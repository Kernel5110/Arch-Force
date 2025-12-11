#!/bin/bash
# Start ngrok in the background
nohup ngrok http 8085 > /dev/null 2>&1 &

echo "Waiting for ngrok to initialize..."
sleep 3

# Fetch the public URL from ngrok's local API
PUBLIC_URL=$(curl -s http://localhost:4040/api/tunnels | grep -o "https://[a-zA-Z0-9-]*\.ngrok-free\.[a-z]*")

if [ -z "$PUBLIC_URL" ]; then
    echo "Error: Could not retrieve ngrok URL. Make sure ngrok is installed and authorized."
else
    echo "Your application is exposed at:"
    echo "$PUBLIC_URL"
fi
