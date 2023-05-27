# Base image
FROM ubuntu:20.04

# Set the working directory inside the container
WORKDIR /app

# Install Python and other dependencies
RUN apt-get update && \
    apt-get install -y python3 python3-pip && \
    rm -rf /var/lib/apt/lists/*

# Copy the code file to the working directory
COPY code.py .

# Copy the necessary files
COPY username.txt .
COPY password.txt .
COPY index.txt .
COPY mapping.txt .
COPY data.parquet .

# Install Python libraries
RUN pip3 install elasticsearch pyarrow pandas

# Set environment variables (optional)
ENV USERNAME=""
ENV PASSWORD=""

# Run the code when the container starts
CMD ["python3", "code.py"]
