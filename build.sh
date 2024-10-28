#!/usr/bin/env bash
# currently assumes and builds only x86_64. 

# Set default image tag and name
IMAGE_TAG="local1"
IMAGE_NAME="ph-ee-integration-test"
ARCH="x86_64"
export DOCKER_CLI_EXPERIMENTAL=enabled
export BUILDX_EXPERIMENTAL=1

# docker buildx build . 
# env 

# Function to build an image for a specific architecture
build_image() {
  local ARCH=$1  # Architecture (e.g., x86_64, arm64)
  echo "Building image for architecture: $ARCH"
  #docker buildx build  .
  docker buildx build \
      --platform "linux/${ARCH}" \
      -t "$IMAGE_NAME:$IMAGE_TAG" .
}

# Build for both architectures sequentially
build_image "$ARCH"
#build_image "$(if [[ "$ARCH" == x86_64 ]]; then echo arm64; else echo x86_64; fi)"

echo "Build completed for x86_64 architecture "