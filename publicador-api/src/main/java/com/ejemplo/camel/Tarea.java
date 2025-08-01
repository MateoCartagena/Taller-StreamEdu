package com.ejemplo.camel;

public class Tarea {
    private String estudiante;
    private String curso;
    private String archivo;
    private String fechaEnvio;

    
    public Tarea() {
    }

    
    public String getEstudiante() {
        return estudiante;
    }

    public void setEstudiante(String estudiante) {
        this.estudiante = estudiante;
    }

    public String getCurso() {
        return curso;
    }

    public void setCurso(String curso) {
        this.curso = curso;
    }

    public String getArchivo() {
        return archivo;
    }

    public void setArchivo(String archivo) {
        this.archivo = archivo;
    }

    public String getFechaEnvio() {
        return fechaEnvio;
    }

    public void setFechaEnvio(String fechaEnvio) {
        this.fechaEnvio = fechaEnvio;
    }

    
    @Override
    public String toString() {
        return "Tarea{" +
                "estudiante='" + estudiante + '\'' +
                ", curso='" + curso + '\'' +
                ", archivo='" + archivo + '\'' +
                ", fechaEnvio='" + fechaEnvio + '\'' +
                '}';
    }
}