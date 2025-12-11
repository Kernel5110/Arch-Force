package com.collaborativeeditor.module1.creation.factory;

import com.collaborativeeditor.module1.creation.model.Element;
import com.collaborativeeditor.module1.creation.model.Table;
import java.util.List;
import java.util.Map;

/**
 * Factory for creating Table elements.
 * 
 * @author Arch_Force Team
 */
public class TableFactory implements ElementFactory {

    private List<String> headers;
    private List<List<String>> rows;

    public TableFactory() {
        this.headers = List.of();
        this.rows = List.of();
    }

    public TableFactory withHeaders(List<String> headers) {
        this.headers = headers;
        return this;
    }

    public TableFactory withRows(List<List<String>> rows) {
        this.rows = rows;
        return this;
    }

    @Override
    public Element createElement() {
        Table table = Table.builder()
                .headers(headers)
                .build();
        if (rows != null) {
            table.setRowsFromList(rows);
        }
        return table;
    }

    @Override
    public String getElementType() {
        return "table";
    }
}
