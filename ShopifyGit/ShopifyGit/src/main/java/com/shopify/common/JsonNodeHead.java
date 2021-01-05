package com.shopify.common;

import com.fasterxml.jackson.databind.JsonNode;

import lombok.Data;

@Data
public class JsonNodeHead {
	private JsonNode jsonNode; 
	private String link;
}
