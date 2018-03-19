package model.util;

import java.io.BufferedReader;

public interface FileTableInterface extends MyDictionaryInterface<Integer,Pair<String,BufferedReader>>
{
    Integer newID();
    boolean isFileOpened(String filename);
}
