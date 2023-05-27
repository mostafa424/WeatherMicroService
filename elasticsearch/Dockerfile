# Base image
FROM ubuntu:20.04

# Set environment variables
ENV ELASTICSEARCH_VERSION=7.15.0
ENV ES_JAVA_OPTS="-Xms512m -Xmx512m"

# Install dependencies
RUN apt-get update && \
    apt-get install -y openjdk-11-jre-headless wget && \
    rm -rf /var/lib/apt/lists/*

# Download and install Elasticsearch
RUN wget https://artifacts.elastic.co/downloads/elasticsearch/elasticsearch-${ELASTICSEARCH_VERSION}-linux-x86_64.tar.gz && \
    wget https://artifacts.elastic.co/downloads/elasticsearch/elasticsearch-${ELASTICSEARCH_VERSION}-linux-x86_64.tar.gz.sha512 && \
    shasum -a 512 -c elasticsearch-${ELASTICSEARCH_VERSION}-linux-x86_64.tar.gz.sha512 && \
    tar -xzf elasticsearch-${ELASTICSEARCH_VERSION}-linux-x86_64.tar.gz && \
    rm elasticsearch-${ELASTICSEARCH_VERSION}-linux-x86_64.tar.gz elasticsearch-${ELASTICSEARCH_VERSION}-linux-x86_64.tar.gz.sha512 && \
    mv elasticsearch-${ELASTICSEARCH_VERSION} /usr/share/elasticsearch

# Set Elasticsearch configuration
RUN echo "network.host: 0.0.0.0" >> /usr/share/elasticsearch/config/elasticsearch.yml && \
    echo "discovery.type: single-node" >> /usr/share/elasticsearch/config/elasticsearch.yml

# Expose Elasticsearch port
EXPOSE 9200

# Set the command to start Elasticsearch
CMD ["/usr/share/elasticsearch/bin/elasticsearch"]
