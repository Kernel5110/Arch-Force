#!/bin/bash

# Kill previous instances to avoid conflicts
pkill -f "spring-boot:run"
# Use specific pattern to avoid killing this script (which has 'ngrok' in name)
pkill -f "ngrok http"

echo "🚀 Starting Arch_Force System..."
echo "--------------------------------"

# Start Spring Boot in the background
echo "⏳ [1/3] Initializing Spring Boot Backend (Maven)..."
echo "    > Logs redirected to backend.log"
nohup ./mvnw spring-boot:run > backend.log 2>&1 &
BACKEND_PID=$!

# Wait for the application to be ready
echo "⏳ [2/3] Waiting for Database Connection and Server Startup..."
echo -n "    > Loading"

# Loop until we see the success message in the logs
# timeout after 60 seconds
count=0
while ! grep -q "Started CollaborativeEditorApplication" backend.log; do
    sleep 2
    echo -n "."
    count=$((count+1))
    if [ $count -ge 60 ]; then
        echo ""
        echo "❌ Error: Timed out waiting for Spring Boot to start."
        echo "Check backend.log for details."
        exit 1
    fi
done

echo ""
echo "✅ Backend is READY on port 8085!"

# Start ngrok
echo "⏳ [3/3] Establishing Secure Tunnel (Ngrok)..."
nohup ngrok http 8085 > /dev/null 2>&1 &
sleep 5

# Fetch the public URL from ngrok's local API
PUBLIC_URL=$(curl -s http://localhost:4040/api/tunnels | grep -o "https://[a-zA-Z0-9-]*\.ngrok-free\.[a-z]*" | head -n 1)

if [ -z "$PUBLIC_URL" ]; then
    echo "❌ Error: Could not retrieve ngrok URL. Make sure ngrok is completed installed and authenticated."
    echo "Run 'ngrok config add-authtoken <TOKEN>' if you haven't already."
else
    echo ""
    echo "==========================================================="
    echo "       ✅ ARCH_FORCE IS ONLINE & PUBLICLY ACCESSIBLE"
    echo "==========================================================="
    echo "   🔗 Public URL:  $PUBLIC_URL"
    echo "   🏠 Local URL:   http://localhost:8085"
    echo "==========================================================="
    echo "Press Ctrl+C to stop the server."
    
    # Keep script running to maintain processes
    wait $BACKEND_PID
fi
