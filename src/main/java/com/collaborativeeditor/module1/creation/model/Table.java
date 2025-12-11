package com.collaborativeeditor.module1.creation.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import java.util.List;
import java.util.ArrayList;
import java.util.stream.Collectors;

/**
 * Concrete implementation of Element representing a table.
 * 
 * @author Arch_Force Team
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@DiscriminatorValue("table")
@EqualsAndHashCode(callSuper = true)
public class Table extends Element {

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "table_headers", joinColumns = @JoinColumn(name = "table_id"))
    @Column(name = "header")
    private List<String> headers;

    // Simplifying: Storing rows as JSON-like strings for finding MVP solution
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "table_rows", joinColumns = @JoinColumn(name = "table_id"))
    @Column(name = "row_data")
    private List<String> rows;

    public void setRowsFromList(List<List<String>> listRows) {
        if (listRows != null) {
            this.rows = listRows.stream()
                    .map(row -> String.join("|||", row)) // delimiter
                    .collect(Collectors.toList());
        }
    }

    public List<List<String>> getRowsAsList() {
        if (rows == null)
            return new ArrayList<>();
        return rows.stream()
                .map(row -> List.of(row.split("\\|\\|\\|")))
                .collect(Collectors.toList());
    }

    @Override
    public String getType() {
        return "table";
    }

    @Override
    public String getContent() {
        return "Table with " + (rows != null ? rows.size() : 0) + " rows";
    }

    @Override
    public String render() {
        StringBuilder sb = new StringBuilder();
        sb.append("<table>");
        if (headers != null && !headers.isEmpty()) {
            sb.append("<thead><tr>");
            for (String h : headers)
                sb.append("<th>").append(h).append("</th>");
            sb.append("</tr></thead>");
        }
        sb.append("<tbody>");
        if (rows != null) {
            for (String rowStr : rows) {
                sb.append("<tr>");
                for (String cell : rowStr.split("\\|\\|\\|")) {
                    sb.append("<td>").append(cell).append("</td>");
                }
                sb.append("</tr>");
            }
        }
        sb.append("</tbody></table>");
        return sb.toString();
    }
}
