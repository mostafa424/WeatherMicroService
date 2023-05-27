package org.example;

import org.apache.avro.Schema;
import org.apache.avro.generic.GenericData;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.parquet.avro.AvroParquetWriter;
import org.apache.parquet.hadoop.ParquetFileWriter;
import org.apache.parquet.hadoop.metadata.CompressionCodecName;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class ParquetWriter {

    public static Schema parseSchema() {
        Schema.Parser parser = new	Schema.Parser();
        Schema schema = null;
        try {
            // Path to schema file
            schema = parser.parse(new File("src/main/java/org/example/schema.avsc"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        return schema;
    }

    public static void writeToParquetFile(List<GenericData.Record> recordList, int station_id) {
        String fileName = "Station_" + station_id + "_" + System.currentTimeMillis() + ".parquet";
        writeToParquetFile(recordList, parseSchema(), fileName);
    }

    public static void writeToParquetFile(List<GenericData.Record> recordList, Schema schema, String fileName) {
        File file = new File("./parquet_files/" + fileName);
        if(file.exists())
            file.delete();

        Path path =	new	Path("./parquet_files/" + fileName);
        org.apache.parquet.hadoop.ParquetWriter<GenericData.Record> writer = null;
        // Creating ParquetWriter using builder
        try {
            writer = AvroParquetWriter
                    .<GenericData.Record>builder(path)
                    .withSchema(schema)
                    .withConf(new Configuration())
                    .withCompressionCodec(CompressionCodecName.SNAPPY)
                    .withWriteMode(ParquetFileWriter.Mode.OVERWRITE)
                    .build();
            // writing records
            for (GenericData.Record record : recordList) {
                System.out.println(record);
                writer.write(record);
            }
        }catch(IOException e) {
            e.printStackTrace();
        }finally {
            if(writer != null) {
                try {
                    writer.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}