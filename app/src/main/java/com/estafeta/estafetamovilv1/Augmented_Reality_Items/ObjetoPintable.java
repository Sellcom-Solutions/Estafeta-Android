package com.estafeta.estafetamovilv1.Augmented_Reality_Items;

/**
 * Created by juan.guerra on 22/06/2015.
 * @deprecated
 */
public class ObjetoPintable {
    private int Xmax;
    private int Xmin;
    private int Ymax;
    private int Ymin;

    public ObjetoPintable() {
    }

    public boolean verificarColisionMarcador(Marcador marcador){
        boolean colP1,colP2,colP3,colP4;
        colP1=this.verificarColisionPunto(marcador.getXmin(), marcador.getYmin());
        colP2=this.verificarColisionPunto(marcador.getXmin(), marcador.getYmax());
        colP3=this.verificarColisionPunto(marcador.getXmax(), marcador.getYmin());
        colP4=this.verificarColisionPunto(marcador.getXmax(), marcador.getYmax());

        if ( colP1||colP2||colP3||colP4 )
            return true;
        else{

            colP1=marcador.verificarColisionPunto(this.getXmin(), this.getYmin());
            colP2=marcador.verificarColisionPunto(this.getXmin(), this.getYmax());
            colP3=marcador.verificarColisionPunto(this.getXmax(), this.getYmin());
            colP4=marcador.verificarColisionPunto(this.getXmax(), this.getYmax());

            if ( colP1||colP2||colP3||colP4 )
                return true;

            return false;
        }
    }

    public boolean verificarColisionPunto(int posX,int posY){
        if (this.Xmin<=posX&&posX<=this.Xmax){
            if (this.Ymin<=posY&&posY<=this.Ymax){
                return true;
            }
            return false;
        }
        return false;
    }

    public boolean verificarColisionMarcadorIcono(Marcador marcador){
        boolean colP1,colP2,colP3,colP4;
        colP1=this.verificarColisionPunto(marcador.getXmin(), marcador.getYmin());
        colP2=this.verificarColisionPunto(marcador.getXmin(), marcador.getYmax());
        colP3=this.verificarColisionPunto(marcador.getXmax(), marcador.getYmin());
        colP4=this.verificarColisionPunto(marcador.getXmax(), marcador.getYmax());

        if ( colP1||colP2||colP3||colP4 )
            return true;
        else{

            colP1=marcador.verificarColisionPunto(this.getXmin(), this.getYmin());
            colP2=marcador.verificarColisionPunto(this.getXmin(), this.getYmax());
            colP3=marcador.verificarColisionPunto(this.getXmax(), this.getYmin());
            colP4=marcador.verificarColisionPunto(this.getXmax(), this.getYmax());

            if ( colP1||colP2||colP3||colP4 )
                return true;

            return false;
        }
    }

    public boolean verificarColisionPuntoMI(int posX,int posY){//Metodo que sera llamado por un marcador
        //Para comprobar su interseccion con una imagen
        if (this.Xmin<=posX&&posX<=this.Xmax){
            if (this.Ymin<=posY&&posY<=this.Ymax){
                return true;
            }
            return false;
        }
        return false;
    }

    public void setValoresX(int X,int Ancho){
        this.Xmax=X+Ancho;
        this.Xmin=X;
    }
    public void setValoresY(int Y,int Largo){
        this.Ymax=Y+Largo;
        this.Ymin=Y;
    }

    public int getXmax() {
        return Xmax;
    }

    public void setXmax(int xmax) {
        Xmax = xmax;
    }

    public int getXmin() {
        return Xmin;
    }

    public void setXmin(int xmin) {
        Xmin = xmin;
    }

    public int getYmax() {
        return Ymax;
    }

    public void setYmax(int ymax) {
        Ymax = ymax;
    }

    public int getYmin() {
        return Ymin;
    }

    public void setYmin(int ymin) {
        Ymin = ymin;
    }

}
