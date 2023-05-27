import platform
from elasticsearch import Elasticsearch
from elasticsearch.transport import Transport
import pyarrow.parquet as pq
import json

# Check the operating system
is_ubuntu = True
# platform.system() == "Linux"
def read_file(path):
    with open(path, "r") as file:
        content = file.read().strip()
    return content

# Connect to Elasticsearch
if is_ubuntu:
    es = Elasticsearch("http://localhost:9200/")
else:
    # Take username and password from files
    username_path = "username.txt"
    password_path = "password.txt"
    username = read_file(username_path)
    password = read_file(password_path)
    #
    # # Create a JSON payload for the new user with admin privileges
    # new_user_payload = {
    #     "password": password,
    #     "roles": ["superuser"],
    #     "full_name": "New Admin User"
    # }
    #
    # # Create or update the new user
    # transport = Transport([{"host": "localhost", "port": 9200}], http_auth=(username, password))
    # try:
    #     transport.perform_request("PUT", f"_security/user/{username}", body=new_user_payload)
    #     print("New user created or updated successfully.")
    # except Exception as e:
    #     print("Error creating or updating the user:", e)

    # Log in with the new user's credentials
    try:
        es = Elasticsearch("http://localhost:9200/", http_auth=(username, password))
        print("Logged in .")
    except Exception as e:
        print("Error logging in: ", e)


# Ask for index name
index_name = read_file("index.txt")

# Ask for mapping file path
mapping = json.loads(read_file("mapping.txt"))

# Create index with mapping
try:
    es.indices.create(index=index_name, body=mapping)
except Exception as e:
    print("Error creating index:", e)
    exit()

# Read parquet file
table = pq.read_table("data.parquet")
df = table.to_pandas()

# Push content to Elasticsearch
for row in df.itertuples():
    document = row._asdict()
    try:
        es.index(index=index_name, body=document)
    except Exception as e:
        print("Error indexing document:", e)