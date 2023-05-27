import platform
from elasticsearch import Elasticsearch
import pyarrow.parquet as pq

# Check the operating system
is_ubuntu = platform.system() == "Linux"

# Connect to Elasticsearch
if is_ubuntu:
    es = Elasticsearch("http://localhost:9200/")
    print("Logged in .")
else:
    # Take username and password from files
    username = input("username: ")
    password = input("password: ")
    # Log in with the new user's credentials
    try:
        es = Elasticsearch("http://localhost:9200/", basic_auth=(username, password))
        print("Logged in .")
    except Exception as e:
        print("Error logging in: ", e)

# Ask for index name
index_name = input("index: ")

# Ask for mapping file path
mapping = {
    "mappings": {
        "properties": {
            "battery_status": {"type": "text"},
            "humidity": {"type": "integer"},
            "s_no": {"type": "integer"},
            "station_id": {"type": "integer"},
            "status_timestamp": {"type": "integer"},
            "temperature": {"type": "integer"},
            "wind_speed": {"type": "integer"}
        }
    }
}

# Create index with mapping
try:
    es.indices.create(index=index_name, body=mapping)
except Exception as e:
    print("Error creating index:", e)
    exit()

# Read parquet file
f_name = input("parquet path: ")
table = pq.read_table(f_name)
df = table.to_pandas()

# Push content to Elasticsearch
for row in df.itertuples():
    document = row._asdict()
    try:
        es.index(index=index_name, body=document)
    except Exception as e:
        print("Error indexing document:", e)