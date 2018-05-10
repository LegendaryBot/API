package com.greatmancode.legendarybotapi.search;

import com.amazonaws.protocol.json.internal.QueryParamMarshallers;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.BinaryDocValuesField;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.RAMDirectory;
import org.json.JSONObject;

import java.io.IOException;

public class SearchHelper {

    private static RAMDirectory ramDirectory = new RAMDirectory();
    private static Analyzer analyzer = new StandardAnalyzer();

    public static JSONObject getRealmInfo(String name) {
        JSONObject jsonObject = null;
        try {
            IndexReader reader = DirectoryReader.open(ramDirectory);
            IndexSearcher searcher = new IndexSearcher(reader);
            QueryParser qp = new QueryParser("name", analyzer);
            Query query = qp.parse(name);
            TopDocs docs = searcher.search(query,1);
            if (docs.totalHits > 0) {
                Document document = searcher.doc(docs.scoreDocs[0].doc);
                jsonObject = new JSONObject();
                jsonObject.put("name", document.get("name"));
                jsonObject.put("slug", document.get("slug"));
                jsonObject.put("type", document.get("type"));
                jsonObject.put("timezone", document.get("timezone"));
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }

    public static boolean isSearchLoaded() {
        boolean result = false;
        try {
            IndexReader reader = DirectoryReader.open(ramDirectory);
            result = reader.numDocs() != 0;
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }
    public static void addRealm(JSONObject realm) {
        IndexWriterConfig indexWriterConfig = new IndexWriterConfig(analyzer);
        indexWriterConfig.setOpenMode(IndexWriterConfig.OpenMode.CREATE_OR_APPEND);

        try {
            IndexWriter writer = new IndexWriter(ramDirectory, indexWriterConfig);
            Document doc = new Document();
            doc.add(new TextField("type", realm.getString("type"), Field.Store.YES));
            doc.add(new TextField("name", realm.getString("name"), Field.Store.YES));
            doc.add(new TextField("slug", realm.getString("slug"), Field.Store.YES));
            doc.add(new TextField("timezone", realm.getString("timezone"), Field.Store.YES));
            writer.addDocument(doc);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
