import platform
from elasticsearch import Elasticsearch
import pyarrow.parquet as pq
import pandas as pd
import json

# Check the operating system
is_ubuntu = platform.system() == "Linux"

def read_file(path):
    with open(path, "r") as file:
        content = file.read()
    return content
# Connect to Elasticsearch
if is_ubuntu:
    es = Elasticsearch("http://localhost:9200/")
else:
    # Take username and password as input
    username = input("Enter username: ")
    password = input("Enter password: ")
    try:
        es = Elasticsearch(f"http://{username}:{password}@localhost:9200/")
    except Exception as e:
        print("Error connecting to Elasticsearch:", e)
        exit()

# Ask for index name
index_name = input("Enter index name: ")

# Ask for parquet file path
parquet_file_path = input("Enter parquet file path: ")

# Ask for mapping file path
mapping_file_path = input("Enter mapping file path: ")

# Read mapping from file
with open(mapping_file_path, "r") as mapping_file:
    mapping_content = mapping_file.read()

# Convert mapping content to a dictionary
mapping = json.loads(mapping_content)

# Create index with mapping
try:
    es.indices.create(index=index_name, body=mapping)
except Exception as e:
    print("Error creating index:", e)
    exit()

# Read parquet file
table = pq.read_table(parquet_file_path)
df = table.to_pandas()
# Push content to Elasticsearch
for row in df.itertuples():
    document = row._asdict()
    try:
        es.index(index=index_name, body=document)
    except Exception as e:
        print("Error indexing document:", e)
