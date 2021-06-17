package com.example.bcraestadisticas.estadistica;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

public class Estadistica implements Serializable, Comparator<Estadistica> {
    public static final String DOLAR_OFICIAL = "Dolar Oficial";
    public static final String UVA = "UVA";
    private LocalDate fecha;
    private Double valor;
    public static List<Estadistica> estadisticaDolarOficial;
    public static List<Estadistica> estadisticaUva;

    public Estadistica() { }

    public Estadistica(LocalDate fecha, Double valor) {
        this.fecha = fecha;
        this.valor = valor;
    }

    public LocalDate getFecha() {
        return this.fecha;
    }

    public String getFechaString(){
        return this.fecha.getDayOfMonth() + "/" + this.fecha.getMonthValue() + "/" + this.fecha.getYear();
    }


    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public Double getValor() {
        return valor;
    }

    public void setValor(Double valor) {
        this.valor = valor;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Estadistica)) return false;
        Estadistica that = (Estadistica) o;
        return getFecha().equals(that.getFecha()) &&
                getValor().equals(that.getValor());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getFecha(), getValor())*37;
    }

    @Override
    public String toString() {
        return "Estadistica{" +
                ", fecha=" + fecha +
                ", valor=" + valor +
                '}';
    }

    public static Double getUltimoValorDeUnaEstadistica(String divisa){
        if("Dolar Oficial".equals(divisa)){
            return Estadistica.estadisticaDolarOficial.get(0).getValor();
        }
        else if("UVA".equals(divisa)){
            return Estadistica.estadisticaUva.get(0).getValor();
        }
        else {
            return new Double(0);
        }
    }

    @Override
    public int compare(Estadistica estadistica, Estadistica t1) {
        return estadistica.getFecha().compareTo(t1.getFecha())*(-1);
    }

}

