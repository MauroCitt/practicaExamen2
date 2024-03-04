package com.example.practicafirebase.models;

public class Coche {
    private String matricula;
    private String marca;
    private String modelo;
    private Persona persona;

    public Coche() {
    }
    public Coche(String matricula, String marca, String modelo, Persona persona) {
        this.matricula = matricula;
        this.marca = marca;
        this.modelo = modelo;
        this.persona = persona;
    }

    public String getMatricula() {
        return matricula;
    }
    public void setMatricula(String matricula) {
        this.matricula = matricula;
    }

    public String getMarca() {
        return marca;
    }
    public void setMarca(String marca) {
        this.marca = marca;
    }

    public String getModelo() {
        return modelo;
    }
    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    public Persona getPersona() {
        return persona;
    }
    public void setPersona(Persona persona) {
        this.persona = persona;
    }
}
