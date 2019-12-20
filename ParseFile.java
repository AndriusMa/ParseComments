package com.library;
import java.io.*;
import java.nio.charset.StandardCharsets;

public class ParseFile {
    private File[] files;
    private File output = new File("Result.txt");
    private FileWriter fileWriter;
    private String temp;
    private String path = System.getProperty("user.dir");
    private String extension; // file type extension, example: .html
    private boolean quotation; // if comment symbol is inside string

    public ParseFile(){
        this.temp = "";
        this.extension = "";
        this.quotation = false;
        this.path = this.path + "\\src\\com\\ba";// setting directory
        files = new File(path).listFiles();
    }

    public void getFileName() throws IOException{
        try{
            output.createNewFile();
            fileWriter = new FileWriter(output);
        }
        catch(IOException e){
            e.printStackTrace();
        }
        showFiles(files);
    }

    private void showFiles(File[] files) throws IOException{
        fileWriter = new FileWriter(output, false);

        if(files == null){
            System.out.println("Incorrect path");
        }
        else {
            for (File file : files) {
                if (!file.isDirectory()) {
                    int i = file.getName().lastIndexOf('.');
                    if (i > 0) {
                        extension = file.getName().substring(i + 1);
                    }
                    switch (extension) { // method call depends on file extension
                        case "cs":
                            parseCsAndJs(file.getName(), fileWriter);
                            break;
                        case "css":
                            parseCss(file.getName(), fileWriter);
                            break;
                        case "cshtml":
                            parseCsHtml(file.getName(), fileWriter);
                            break;
                        case "html":
                            parseHtmlAndCsProj(file.getName(), fileWriter);
                            break;
                        case "csproj":
                            parseHtmlAndCsProj(file.getName(), fileWriter);
                            break;
                        case "js":
                            parseCsAndJs(file.getName(), fileWriter);
                            break;
                        case "json":
                            fileWriter.write("\n" + "======" + file.getName() + "======" + "\n");
                            fileWriter.write("Comments in .json files are not allowed\n\n");
                            break;
                        default:
                            fileWriter.write("\n" + "======" + file.getName() + "======" + "\n");
                            fileWriter.write("Parsing is not implemented for ." + extension + " file type\n\n");
                    }
                }
            }
        }
        fileWriter.close();
    }

    private void parseCsAndJs(String fileName, FileWriter fileWriter) throws IOException {
        File directory = new File(path + "\\" + fileName);
        fileWriter.write("\n" + "======" + fileName + "======" + "\n");
        BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(directory.getAbsolutePath()), StandardCharsets.UTF_8));
        int counter = 1;
        this.quotation = false;
        try {
            int c;
            while ((c = reader.read()) != -1) { // if it's EOF
                temp = "";
                char currChar = (char) c;
                if (currChar == '\"') {
                    if(this.quotation) {
                        this.quotation = false;
                    }
                    else {
                        this.quotation = true;
                    }
                }
                else if (currChar == '/' && !this.quotation) {
                    c = reader.read();
                    currChar = (char) c;
                    if (currChar == '/' && c != -1) {
                        temp += "//";
                        while ((c = reader.read()) != '\n') {
                            currChar = (char) c;
                            temp += currChar;
                        }
                        fileWriter.write(counter + ". " + temp + "\n");
                        counter++;
                    } else if (currChar == '*' && c != -1) {
                        temp += "/*";
                        while (true) {
                            c = reader.read();
                            currChar = (char) c;
                            temp += currChar;
                            if (currChar == '*') {
                                c = reader.read();
                                currChar = (char) c;
                                temp += currChar;
                                if (currChar == '/') {
                                    fileWriter.write(counter + ". " + temp + "\n");
                                    counter++;
                                    break;
                                }
                            } else if (c == -1) {
                                break;
                            }

                        }
                    }
                }
            }
        }
        catch(IOException e){
            e.printStackTrace();
        }
        if(counter == 1){
            fileWriter.write("Couldn't find any comments\n");
        }
    }

    private void parseHtmlAndCsProj(String fileName, FileWriter fileWriter) throws IOException {
        File directory = new File(path + "\\" + fileName);
        fileWriter.write("\n" + "======" + fileName + "======" + "\n");
        BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(directory.getAbsolutePath()), StandardCharsets.UTF_8));
        int counter = 1;
        try{
            int c;
            while ((c = reader.read()) != -1) { // if it's EOF
                temp = "";
                char currChar = (char) c;
                if (currChar == '<') {
                    c = reader.read();
                    currChar = (char) c;
                    if (currChar == '!' && c != -1) {
                        c = reader.read();
                        currChar = (char) c;
                        if (currChar == '-' && c != -1) {
                            c = reader.read();
                            currChar = (char) c;
                            if (currChar == '-' && c != -1) {
                                temp += "<!--";
                                while (true) {
                                    c = reader.read();
                                    currChar = (char) c;
                                    temp += currChar;
                                    if(currChar == '-'){
                                        c = reader.read();
                                        currChar = (char) c;
                                        temp += currChar;
                                        if(currChar == '-'){
                                            c = reader.read();
                                            currChar = (char) c;
                                            temp += currChar;
                                        }
                                        if(currChar == '>'){
                                            fileWriter.write(counter + ". " + temp + "\n");
                                            counter++;
                                            break;
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        catch(IOException e){
            e.printStackTrace();
        }

        if(counter == 1){
            fileWriter.write("Couldn't find any comments\n");
        }
    }

    private void parseCsHtml(String fileName, FileWriter fileWriter) throws IOException{
        File directory = new File(path + "\\" + fileName);
        fileWriter.write("\n" + "======" + fileName + "======" + "\n");
        BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(directory.getAbsolutePath()), StandardCharsets.UTF_8));
        int counter = 1;
        this.quotation = false;
        try {
            int c;
            while ((c = reader.read()) != -1) { // if it's EOF
                temp = "";
                char currChar = (char) c;
                if (currChar == '\"') {
                    if(this.quotation) {
                        this.quotation = false;
                    }
                    else {
                        this.quotation = true;
                    }
                }
                else if (currChar == '@' && !this.quotation) {
                    c = reader.read();
                    currChar = (char) c;
                    if (currChar == '*' && c != -1) {
                        temp += "@*";
                        while (true) {
                            c = reader.read();
                            currChar = (char) c;
                            temp += currChar;

                            if (currChar == '*') {
                                c = reader.read();
                                currChar = (char) c;
                                temp += currChar;
                                if (currChar == '@') {
                                    fileWriter.write(counter + ". " + temp + "\n");
                                    counter++;
                                    break;
                                }
                            }
                        }
                    }
                }
            }
        }
        catch(IOException e){
            e.printStackTrace();
        }

        if(counter == 1){
            fileWriter.write("Couldn't find any comments\n");
        }
    }

    private void parseCss(String fileName, FileWriter fileWriter) throws IOException{
        File directory = new File(path + "\\" + fileName);
        fileWriter.write("\n" + "======" + fileName + "======" + "\n");
        BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(directory.getAbsolutePath()), StandardCharsets.UTF_8));
        int counter = 1;
        this.quotation = false;
        try {
            int c;
            while ((c = reader.read()) != -1) { // if it's EOF
                temp = "";
                char currChar = (char) c;
                if (currChar == '\"') {
                    if(this.quotation) {
                        this.quotation = false;
                    }
                    else {
                        this.quotation = true;
                    }
                }
                else if (currChar == '/' && !this.quotation) {
                    c = reader.read();
                    currChar = (char) c;

                    if (currChar == '*' && c != -1) {
                        temp += "/*";
                        while (true) {
                            c = reader.read();
                            currChar = (char) c;
                            temp += currChar;
                            if (currChar == '*') {
                                c = reader.read();
                                currChar = (char) c;
                                temp += currChar;
                                if (currChar == '/') {
                                    fileWriter.write(counter + ". " + temp + "\n");
                                    counter++;
                                    break;
                                }
                            }
                        }
                    }
                }
            }
        }
        catch(IOException e){
            e.printStackTrace();
        }

        if(counter == 1){
            fileWriter.write("Couldn't find any comments\n");
        }
    }
}
