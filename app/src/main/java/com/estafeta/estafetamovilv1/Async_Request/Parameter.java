package com.estafeta.estafetamovilv1.Async_Request;

/**
 * Created by juan.guerra on 10/07/2015.
 * @deprecated
 */
public class Parameter {
    public enum BUY_REQUEST { //Pendiente
        COSTO ("costo"),
        NOMBRE_REMITENTE ("nombre_remitente"),
        TELEFONO_REMITENTE ("telefono_remitente"),
        EMAIL_REMITENTE ("email_remitente"),
        RAZON_REMITENTE ("razon_remitente"),
        CP_ORIGEN ("cp_origen"),
        ESTADO_ORIGEN ("estado_origen"),
        CIUDAD_ORIGEN ("ciudad_origen"),
        COLONIA_ORIGEN ("colonia_origen"),
        CALLE_ORIGEN ("calle_origen"),
        NO_ORIGEN ("no_origen"),
        NOMBRE_DESTINATARIO ("nombre_destinatario"),
        TELEFONO_DESTINATARIO ("telefono_destinatario"),
        EMAIL_DESTINATARIO ("email_destinatario"),
        RAZON_DESTINATARIO ("razon_destinatario"),
        CP_DESTINO ("cp_destino"),
        ESTADO_DESTINO ("estado_destino"),
        CIUDAD_DESTINO ("ciudad_destino"),
        COLONIA_DESTINO ("colonia_destino"),
        CALLE_DESTINO ("calle_destino"),
        NO_DESTINO ("no_destino"),
        GARANTIA ("garantia");

        private final String name;

        private BUY_REQUEST(String s) {
            name = s;
        }

        public boolean equalsName(String otherName){
            return (otherName == null)? false:name.equals(otherName);
        }

        public String toString(){
            return name;
        }

    }

    public enum BUY_RESPONSE { //Pendiente
        REMITENTE ("remitente"),
        ORIGEN ("origen"),
        CP_ORIGEN ("cp_origen"),
        DESTINATARIO ("destinatario"),
        DESTINO ("destino"),
        CP_DESTINO ("cp_destino"),
        TIPO_SERVICIO ("tipo_servicio"),
        GARANTIA ("garantia"),
        COSTO ("costo"),
        REFERENCIA ("referencia");

        private final String name;

        private BUY_RESPONSE(String s) {
            name = s;
        }

        public boolean equalsName(String otherName){
            return (otherName == null)? false:name.equals(otherName);
        }

        public String toString(){
            return name;
        }

    }

}
