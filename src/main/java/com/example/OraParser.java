package com.example;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;

import edu.gatech.gtri.orafile.Orafile;
import edu.gatech.gtri.orafile.OrafileDef;
import edu.gatech.gtri.orafile.OrafileDict;

public class OraParser {

    public static void main(String[] args) throws Exception {
        File exampleFile = new File("src/test/resources/example/tnsnames.ora");

        List<OracleDb> list = parseOraFile(exampleFile);
        for (OracleDb oracleDb : list) {
            System.out.println(oracleDb);
        }
    }

    public static List<OracleDb> parseOraFile(File file) {
        ArrayList<OracleDb> result = new ArrayList<>();

        try {
            String contents = FileUtils.readFileToString(file, StandardCharsets.ISO_8859_1);

            // parse the file
            OrafileDict tns = Orafile.parse(contents);

            // switch the result into another data structure
            for (OrafileDef def : tns.asList()) {

                OracleDb db = new OracleDb();

                db.setName(def.getName());
                db.setHost(def.getVal().findOneString("HOST"));
                db.setPort(def.getVal().findOneString("PORT"));
                db.setSid(def.getVal().findOneString("SID"));
                db.setServiceName(def.getVal().findOneString("SERVICE_NAME"));

                result.add(db);
            }

        } catch (Exception e) {
            throw new RuntimeException("Could not parse file " + file, e);
        }

        return result;
    }
}
