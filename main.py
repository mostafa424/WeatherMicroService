from elasticsearch import Elasticsearch
import pyarrow.parquet as pq
import pandas as pd
es = Elasticsearch("http://salah:new123@localhost:9200/")
parquet_file_path = "userdata1.parquet"
table = pq.read_table(parquet_file_path)
#print(table.schema)
df = table.to_pandas()
#print(df[0:2])
query = {
    "query": {
        "match_all": {}
    }
}
index_name = "users"
# Execute the search query
response = es.search(index=index_name, body=query)

# Process the search results
for hit in response["hits"]["hits"]:
    document = hit["_source"]
    print(document)
"""index_name = "users"
document_type = "_doc"
mapping = {
    "mappings": {
        "properties": {
            "registration_dttm": {"type": "date"},
            "id": {"type": "integer"},
            "first_name": {"type": "text"},
            "last_name": {"type": "text"},
            "email": {"type": "text"},
            "gender": {"type": "text"},
            "ip_address": {"type": "text"},
            "cc": {"type": "text"},
            "country": {"type": "text"},
            "birthdate": {"type": "text"},
            "salary": {"type": "double"},
            "title": {"type": "text"},
            "comments": {"type": "text"}
        }
    }
}
client.indices.create(index=index_name, body=mapping)
#print(df)
for row in df.itertuples():
    document = row._asdict()
    client.index(index=index_name, document=document)
"""
