# Base image
FROM python:3.9-slim

# Set the working directory inside the container
WORKDIR /app

# Copy the code file to the working directory
COPY code.py .

# Copy the necessary files
COPY username.txt .
COPY password.txt .
COPY index.txt .
COPY mapping.txt .
COPY data.parquet .

# Install dependencies
RUN pip install elasticsearch pyarrow pandas

# Set environment variables (optional)
ENV USERNAME=""
ENV PASSWORD=""

# Run the code when the container starts
CMD ["python", "code.py"]
