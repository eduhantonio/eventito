package com.example.eventito;

import java.util.List;

public class LayoutData {

    public static class LayoutElement {
        private String type;  // Tipo de elemento (ex: "title", "paragraph", "image")
        private String content;  // Conteúdo do elemento (pode ser texto ou outros dados)

        // Construtor para tipo texto
        public LayoutElement(String type, String content) {
            this.type = type;
            this.content = content;
        }
        public LayoutElement() {
        }

        public String getType() {
            return type;
        }

        public String getContent() {
            return content;
        }
    }
}
