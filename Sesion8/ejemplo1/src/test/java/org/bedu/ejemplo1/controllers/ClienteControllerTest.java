package org.bedu.ejemplo1.controllers;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;

import org.bedu.ejemplo1.model.Cliente;

import static org.hamcrest.Matchers.is;


@AutoConfigureRestDocs
@WebMvcTest(ClienteController.class)
public class ClienteControllerTest {

    @Autowired
    private MockMvc mockMvc;
    
    @Test
    void obtenerClienteTest() throws Exception
    {
        mockMvc.perform(get("/cliente/{clienteId}", 1)
            .content(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(status().isOk())
            .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id", is(1)))
            .andExpect(jsonPath("$.correoContacto", is("cliente@contacto.com")))
            .andExpect(jsonPath("$.nombre", is("Nombre")))

            .andDo(document("cliente/get-cliente",
                        pathParameters(
                                parameterWithName("clienteId").description("Identificador del cliente")
                        )));
    }

    @Test
    void crearClienteTest() throws Exception
    {
        Cliente clienteParametro = Cliente.builder().nombre("Nombre").direccion("Direccion").numeroEmpleados(10).correoContacto("contacto@cliente.com").build();

        mockMvc.perform(post("/cliente")
            .contentType(MediaType.APPLICATION_JSON)
            .content(new ObjectMapper().writeValueAsString(clienteParametro)))
            .andExpect(status().isCreated())

            .andDo(document("cliente/post-cliente", 
                requestFields(
                    fieldWithPath("id").description("El identificador del nuevo cliente"),
                                fieldWithPath("nombre").description("El nombre del cliente"),
                                fieldWithPath("direccion").description("La dirección del cliente"),
                                fieldWithPath("correoContacto").description("La dirección de correo electrónico de contacto"),
                                fieldWithPath("numeroEmpleados").description("El número de personas que trabajan en las oficinas e cliente")
                )));
    }
}
