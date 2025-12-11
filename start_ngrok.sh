#!/bin/bash

# =============================================================================
# Arch_Force Startup Script
# Robust, Safe, and Deterministic
# =============================================================================

SERVER_PORT=8085
BACKEND_LOG="backend.log"

# function to clean up processes on exit
cleanup() {
    echo ""
    echo "🛑 Shutting down..."
    
    if [ ! -z "$NGROK_PID" ]; then
        echo "   -> Killing ngrok (PID: $NGROK_PID)"
        kill $NGROK_PID 2>/dev/null
    fi
    
    if [ ! -z "$BACKEND_PID" ]; then
        echo "   -> Killing Spring Boot (PID: $BACKEND_PID)"
        kill $BACKEND_PID 2>/dev/null
        # Wait for it to actually die to be nice
        wait $BACKEND_PID 2>/dev/null
    fi
    
    echo "✅ Cleanup complete."
}

# Trap SIGINT (Ctrl+C) and SIGTERM
trap cleanup SIGINT SIGTERM EXIT

check_port() {
    local port=$1
    if command -v lsof >/dev/null 2>&1; then
        pid=$(lsof -t -i:$port)
    elif command -v fuser >/dev/null 2>&1; then
        pid=$(fuser $port/tcp 2>/dev/null)
    else
        echo "⚠️  Warning: Neither 'lsof' nor 'fuser' found. Cannot check port usage safely."
        return 0
    fi

    if [ ! -z "$pid" ]; then
        echo "⚠️  Port $port is in use by PID $pid."
        read -p "❓ Do you want to kill it? (y/n) " -n 1 -r
        echo ""
        if [[ $REPLY =~ ^[Yy]$ ]]; then
            echo "   -> Killing PID $pid..."
            kill -9 $pid
        else
            echo "❌ Cannot start server while port $port is in use. Exiting."
            # Remove trap to avoid double cleanup message since we haven't started anything yet
            trap - SIGINT SIGTERM EXIT
            exit 1
        fi
    fi
}

# 1. Prerequisite Checks
echo "🚀 Initializing Arch_Force System..."
echo "--------------------------------"

check_port $SERVER_PORT

# 2. Start Spring Boot
echo "⏳ [1/3] Launching Spring Boot Backend..."
echo "    > Output directed to $BACKEND_LOG"

# Clear previous log
> $BACKEND_LOG

nohup ./mvnw spring-boot:run > $BACKEND_LOG 2>&1 &
BACKEND_PID=$!
echo "    > Backend PID: $BACKEND_PID"

# 3. Start Ngrok
echo "⏳ [2/3] Establishing Ngrok Tunnel..."
nohup ngrok http $SERVER_PORT > /dev/null 2>&1 &
NGROK_PID=$!
echo "    > Ngrok PID: $NGROK_PID"

# 4. Wait for Ngrok API (Replacing sleep with polling)
echo -n "    > Waiting for ngrok API"
NGROK_URL=""
count=0
while [ -z "$NGROK_URL" ]; do
    if [ $count -ge 15 ]; then
        echo ""
        echo "❌ Error: Ngrok failed to start or API is unreachable."
        exit 1
    fi
    
    # Try to fetch tunnel URL
    NGROK_URL=$(curl -s http://localhost:4040/api/tunnels | grep -o "https://[a-zA-Z0-9-]*\.ngrok-free\.[a-z]*" | head -n 1)
    
    if [ -z "$NGROK_URL" ]; then
        sleep 1
        echo -n "."
        count=$((count+1))
    fi
done
echo ""
echo "    > Tunnel established!"

# 5. Wait for Backend Readiness (Polling logs)
echo "⏳ [3/3] Waiting for Application Startup..."
echo -n "    > Monitoring logs"

count=0
# Adjust timeout as needed
TIMEOUT=120 
while ! grep -q "Started CollaborativeEditorApplication" $BACKEND_LOG; do
    sleep 2
    echo -n "."
    count=$((count+2))
    
    # Check if backend process is still alive
    if ! kill -0 $BACKEND_PID 2>/dev/null; then
        echo ""
        echo "❌ Error: Backend process died unexpectedly. Check $BACKEND_LOG."
        exit 1
    fi

    if [ $count -ge $TIMEOUT ]; then
        echo ""
        echo "❌ Error: Timed out waiting for Spring Boot."
        exit 1
    fi
done
echo ""

# 6. Success Output
echo "==========================================================="
echo "       ✅ SYSTEM ONLINE & READY"
echo "==========================================================="
echo "   🔗 Public URL:  $NGROK_URL"
echo "   🏠 Local URL:   http://localhost:$SERVER_PORT"
echo "   📄 Logs:        tail -f $BACKEND_LOG"
echo "==========================================================="
echo "Press Ctrl+C to stop services safely."

# 7. Wait
# Reset the EXIT trap to prevent double cleanup when wait exits normally (which it only does on signal here)
# Actually, keep the trap; if wait is interrupted, trap fires.
wait $BACKEND_PID
