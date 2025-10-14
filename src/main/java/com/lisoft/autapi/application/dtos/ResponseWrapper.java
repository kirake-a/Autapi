package com.lisoft.autapi.application.dtos;

public record ResponseWrapper<T>(boolean success, String messsage, T data) {}
