package com.collaborativeeditor.module1.creation.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import java.util.List;

/**
 * Concrete implementation of Element representing a list.
 * Part of the Factory Method pattern.
 * 
 * @author Arch_Force Team
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@DiscriminatorValue("list")
@EqualsAndHashCode(callSuper = true)
public class ListElement extends Element {

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "list_items", joinColumns = @JoinColumn(name = "list_id"))
    @Column(name = "item")
    private List<String> items;

    private boolean ordered;

    @Override
    public String getType() {
        return "list";
    }

    @Override
    public String getContent() {
        return String.join("\n", items);
    }

    @Override
    public String render() {
        StringBuilder sb = new StringBuilder();
        String tag = ordered ? "ol" : "ul";
        sb.append("<").append(tag).append(">");
        for (String item : items) {
            sb.append("<li>").append(item).append("</li>");
        }
        sb.append("</").append(tag).append(">");
        return sb.toString();
    }
}
