package com.estafeta.estafetamovilv1.Augmented_Reality_Items;

import android.app.Activity;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.support.v4.app.FragmentManager;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.ImageSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.text.style.SuperscriptSpan;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

import java.math.RoundingMode;
import java.text.NumberFormat;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import android.location.Location;
import android.view.View.OnClickListener;

import com.estafeta.estafetamovilv1.Fragments.FragmentDialogOfficesMap;
import com.estafeta.estafetamovilv1.R;
import com.estafeta.estafetamovilv1.Utils.DialogManager;
import com.estafeta.estafetamovilv1.Utils.Utilities;
import com.google.android.gms.maps.model.LatLng;


/**
 * Created by juan.guerra on 22/06/2015.
 * @deprecated
 */
public class ContenedorCajas {
    private RelativeLayout contenedor;
    private List<Marcador> lista;
    private int altoC;
    public static int tamLetra;
    private int color_fondo_AR;
    private int color_texto_AR;
    private Activity activity;
    private FragmentManager fragmentManager;
    private int ancho = 0, alto = 0, sizeTextSucursal = 0, sizeImage = 0, anchoImage = 0, altoImage = 0;

    boolean isDialogClosed = true;

    public ContenedorCajas(RelativeLayout contenedor, Activity activity, FragmentManager fragmentManager) {
        this.contenedor=contenedor;
        this.activity = activity;
        this.fragmentManager = fragmentManager;

        lista=new LinkedList<Marcador>();
        float dimm=contenedor.getContext().getResources().getDimension(R.dimen.sizeAugmentedRealityText);
        tamLetra = (int)Pantalla.convertPixelsToSp(dimm,contenedor.getContext());
        Log.d("TAM TEX: ",String.valueOf(tamLetra));
        color_fondo_AR=Color.WHITE;
        color_texto_AR=Color.BLACK;
    }

    public void agregar(CajaDeTexto caja,
                        android.widget.RelativeLayout.LayoutParams parametros,Marcador marcador) {
        // TODO Auto-generated method stub
        contenedor.addView(caja, parametros);
        lista.add(marcador);
    }

    public void agregar(Button elemento,
                        android.widget.RelativeLayout.LayoutParams parametros,Marcador marcador) {
        // TODO Auto-generated method stub
        contenedor.addView(elemento, parametros);
        lista.add(marcador);
    }

    public void agregar(CajaDeTexto caja,
                        android.widget.RelativeLayout.LayoutParams parametros) {
        // TODO Auto-generated method stub
        contenedor.addView(caja, parametros);
    }

    public int numeroElementos(){
        return lista.size();
    }

    public void eliminar(CajaDeTexto caja){
        contenedor.removeView(caja);
        lista.remove(caja);
    }

    public void eliminar(){
        contenedor.removeAllViews();
        lista.clear();
    }

    public void agregarLugaraPantalla(OnClickListener listener,
                                      CajaDeTexto caja,int x,int y,Marcador marcador,Location ubicacion){
        /*Log.d("AGREGAR_RECUPERADO: ",String.valueOf(marcador.getId()));
        Log.d("AGREGAR_RECUPERADO: ",String.valueOf(marcador.getNombre()));
        Log.d("AGREGAR_RECUPERADO: ",String.valueOf(marcador.getTipo_elemento()));
        Log.d("AGREGAR_RECUPERADO: ",String.valueOf(marcador.getLocalizacionLugar().getLatitude()));
        Log.d("AGREGAR_RECUPERADO: ",String.valueOf(marcador.getLocalizacionLugar().getLongitude()));*/

        caja.setId(marcador.getId());
        caja.setTextColor(color_texto_AR);
        float distance=ubicacion.distanceTo(marcador.getLocalizacionLugar());
        String units;
        if (distance>=1000){
            distance=distance/1000;
            units=" Km";
        }
        else{
            units=" m";
        }
        NumberFormat nf=NumberFormat.getInstance();

        nf.setMaximumFractionDigits(1);
        nf.setRoundingMode(RoundingMode.DOWN);
        units=nf.format(distance)+units;

        float sizeA = 0, sizeB = 0, sizeC = 0;

        DisplayMetrics metrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(metrics);
        int widthPixels = metrics.widthPixels;
        int heightPixels = metrics.heightPixels;
        float widthDpi = metrics.xdpi;
        float heightDpi = metrics.ydpi;

        float widthInches = widthPixels / widthDpi;
        float heightInches = heightPixels / heightDpi;

        double diagonalInches = Math.sqrt(
                (widthInches * widthInches)
                        + (heightInches * heightInches));

        if (diagonalInches >= 10) {
            //Device is a 10" tablet

            ancho = (int) (180 * Pantalla.DENSIDAD + 5);
            alto = (int) (130 * Pantalla.DENSIDAD + 5);

            sizeA = 2.2f;
            sizeB = 1.9f;
            sizeC = 1.6f;

            sizeTextSucursal = 15;
            sizeImage = (int) (70 * Pantalla.DENSIDAD + 5);


            anchoImage = (int) (160 * Pantalla.DENSIDAD + 5);
            altoImage = (int) (30 * Pantalla.DENSIDAD + 5);
        }
        else if (diagonalInches >= 7) {
            //Device is a 7" tablet

            ancho = (int) (140 * Pantalla.DENSIDAD + 5);
            alto = (int) (100 * Pantalla.DENSIDAD + 5);

            sizeA = 1.8f;
            sizeB = 1.2f;
            sizeC = 1f;


            sizeTextSucursal = 11;
            sizeImage = (int) (50 * Pantalla.DENSIDAD + 5);

            anchoImage = (int) (110 * Pantalla.DENSIDAD + 5);
            altoImage = (int) (22 * Pantalla.DENSIDAD + 5);

        }else{
            ancho = (int) (100 * Pantalla.DENSIDAD + 5);
            alto = (int) (70 * Pantalla.DENSIDAD + 5);

            sizeA = 1.3f;
            sizeB = 0.8f;
            sizeC = 0.6f;

            sizeTextSucursal = 7;
            sizeImage = (int) (40 * Pantalla.DENSIDAD + 5);

            anchoImage = (int) (80 * Pantalla.DENSIDAD + 5);
            altoImage = (int) (15 * Pantalla.DENSIDAD + 5);
        }



        if(marcador.getNombre().length() > 22){
            marcador.setNombre(marcador.getNombre().substring(0,22)+"...");
        }

        Drawable d = activity.getResources().getDrawable(R.drawable.estafeta_ra);
        d.setBounds(0, 0, anchoImage, altoImage);
        ImageSpan span = new ImageSpan(d, ImageSpan.ALIGN_BASELINE);

        String s= "Estafeta®\n" + marcador.getNombre() + "\nDistancia "+ units;
        SpannableString ss1=  new SpannableString(s);

        /*ss1.setSpan(new RelativeSizeSpan(sizeA), 0, 8, 0); // set size
        ss1.setSpan(new StyleSpan(Typeface.BOLD), 0, 8, 0);
        ss1.setSpan(new ForegroundColorSpan(0xFFA91F20), 0, 8, 0);// set color*/

        ss1.setSpan(span, 0, 9, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);

       /* ss1.setSpan(new RelativeSizeSpan(sizeB), 8, 9, 0); // set size
        ss1.setSpan(new StyleSpan(Typeface.BOLD), 8, 9, 0);
        ss1.setSpan(new SuperscriptSpan(), 8, 9, 0);
        ss1.setSpan(new ForegroundColorSpan(0xFFA91F20), 8, 9, 0);// set color*/

        ss1.setSpan(new RelativeSizeSpan(sizeC), 10, (10+marcador.getNombre().length()), 0); // set size
        ss1.setSpan(new ForegroundColorSpan(Color.BLACK), 10, (10 + marcador.getNombre().length()), 0);// set color

        ss1.setSpan(new RelativeSizeSpan(sizeC), (10 + marcador.getNombre().length()), s.length(), 0); // set size
        ss1.setSpan(new ForegroundColorSpan(Color.GRAY), (10 + marcador.getNombre().length()), s.length(), 0);// set color

        caja.setText(ss1);
        //caja.setText(Html.fromHtml("<b><font color=#A91F20>Estafeta<sup>®</sup></font></b> <br />"+"<font color=#000000>Hipódromo Condesa</font><br />"+"<font color=#888888> Distancia "+units+ "</font>"));
        caja.setX(x);
        caja.setY(y);
        //Fin codigo para posicionar elemento
        //caja.setAlpha((float) 0.5);
        //Buscamos alto y ancho optimo para mostrar el nombre en pantalla
        altoC=caja.obtenerAlto()*(marcador.getNumLineas()+1);
        marcador.setValoresX(x, ancho);
        marcador.setValoresY(y, alto);
        RelativeLayout.LayoutParams parametros=new RelativeLayout.LayoutParams(
                ancho,
                alto);

        caja.setBackgroundResource(R.drawable.background_white_textview_ra);
        caja.setHeight(parametros.height);
        caja.setWidth(parametros.width);

        final Map<String,String> oficina;
        oficina = new HashMap<>();
        oficina.put("contenedor","contenedor");
        oficina.put("nombre", marcador.getNombre().replace("...",""));
        oficina.put("calle1",marcador.getCalle1());
        oficina.put("ciudad_n", marcador.getCiudad_n());
        oficina.put("codigo_postal", marcador.getCodigo_postal());
        oficina.put("horario_atencion", marcador.getHorario_atencion());
        oficina.put("ext1", marcador.getExt1());
        oficina.put("telefono1", marcador.getTelefono1());
        oficina.put("colonia_n", marcador.getColonia_n());
        oficina.put("latitud", marcador.getLatitud());
        oficina.put("longitud", marcador.getLongitud());

        Location loc = marcador.getLocalizacionLugar();

        final LatLng position = new LatLng(loc.getLatitude(), loc.getLongitude());

        String type = "";

        if (Utilities.convertOfficesIntToType(marcador.getTipo_elemento()).equals("SU")) {
            type = "SU";
        } else if (Utilities.convertOfficesIntToType(marcador.getTipo_elemento()).equals("CO")) {
            type = "CO";
        } else if (Utilities.convertOfficesIntToType(marcador.getTipo_elemento()).equals("CA")) {
            type = "CA";
        }


        final String finalType = type;
        caja.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isDialogClosed) {
                    isDialogClosed = false;
                    DialogManager.sharedInstance().showDialog(DialogManager.TYPE_DIALOG.LOADING, "Cargando oficina...", 0);
                    AsyckTask asyckTask = new AsyckTask(oficina, position, finalType);
                    asyckTask.execute();
                }

            }
        });

        Marcador temp;
        Marcador temp2;
        agregar(caja, parametros, marcador);
        Button imagen=new Button(contenedor.getContext());
        imagen.setEnabled(false);
        imagen.setGravity(Gravity.CENTER);
        imagen.setId(caja.getId());
        Bitmap bMap=null;
        bMap=Utilities.getOfficesIcon(Utilities.convertOfficesIntToType(marcador.getTipo_elemento()), contenedor.getContext());
        //bMap=bMap.createScaledBitmap(bMap, altoC, altoC, false);


        /*imagen.setImageBitmap(bMap);
        imagen.setScaleType(ImageView.ScaleType.CENTER_INSIDE);*/

        imagen.setTextColor(activity.getResources().getColor(R.color.estafeta_red));
        imagen.setText("SUCURSAL");
        imagen.setTextSize(TypedValue.COMPLEX_UNIT_PX, dpToPx(sizeTextSucursal));

        Drawable img = new BitmapDrawable(contenedor.getContext().getResources(), bMap);
        img.setBounds(0, 0, sizeImage, sizeImage);
        imagen.setCompoundDrawables(null, img, null, null);

        //imagen.setImageDrawable(activity.getResources().getDrawable(R.drawable.background_white_imageview_ra));
        imagen.setBackgroundResource(R.drawable.button_ra_style);

        //imagen.getBackground().setAlpha(127);
        Marcador ma=new Marcador(marcador.getId(), marcador.getTipo_elemento());

        /*int paddingPixel = 10;
        int paddingDp = (int)(paddingPixel * Pantalla.DENSIDAD+5);
        imagen.setPadding(paddingDp,paddingDp,paddingDp,paddingDp);*/

        imagen.setX(marcador.getXmin() - alto + 20);
        imagen.setY(marcador.getYmin());

        parametros=new RelativeLayout.LayoutParams(alto-20
                ,alto);
        ma.setValoresX(marcador.getXmin()-alto+20, alto);
        ma.setValoresY(marcador.getYmin(), alto);
        agregar(imagen,parametros,ma);
        //Log.d("ContenedorCajas",marcador.getNombre());
        for (int location=0;location<lista.size()-2;location++){
            temp=lista.get(location);
            temp2=lista.get(location+1);
            if (temp.isTipo()){
                while(temp.verificarColisionMarcador(marcador)||marcador.verificarColisionMarcador(temp)
                        ||temp2.verificarColisionMarcador(marcador)||marcador.verificarColisionMarcador(temp2)
                        ||temp.verificarColisionMarcador(ma)||ma.verificarColisionMarcador(temp)){
                    //Log.d("Colision", "Colision");
                    temp=lista.get(location);
                    marcador=lista.get(lista.size()-2);
                    recorrerMarcadores(marcador,temp,ubicacion,lista.size()-2);
                }
            }
        }

        for (int location=0;location<lista.size() - 2; location++) {
            temp=lista.get(location);
            temp2=lista.get(location + 1);
            if (temp.isTipo()) {
                if(temp.verificarColisionMarcador(marcador)|| marcador.verificarColisionMarcador(temp)
                        ||temp2.verificarColisionMarcador(marcador)||marcador.verificarColisionMarcador(temp2)
                        ||temp.verificarColisionMarcador(ma)||ma.verificarColisionMarcador(temp)){
                    //Regresamos a los valores originales
                    caja=(CajaDeTexto)this.contenedor.getChildAt(lista.size()-2);
                    caja.setX(x);
                    contenedor.removeViewAt(lista.size() - 2);
                    parametros=new RelativeLayout.LayoutParams(
                            ancho,
                            alto);
                    //
                    contenedor.addView(caja, lista.size() - 2, parametros);
                    marcador.setValoresX(x, ancho);
                    lista.set(lista.size() - 2, marcador);

                    imagen=(Button)this.contenedor.getChildAt(lista.size()-1);
                    imagen.setGravity(Gravity.CENTER);
                    imagen.setEnabled(false);
                    ma=new Marcador(marcador.getId(), marcador.getTipo_elemento());
                    imagen.setX(marcador.getXmin() - alto + 20);
                    imagen.setY(marcador.getYmin());
                    contenedor.removeViewAt(lista.size() - 1);

                    parametros=new RelativeLayout.LayoutParams(alto-20
                            ,alto);
                    ma.setValoresX(marcador.getXmin()-alto+20, alto);
                    ma.setValoresY(marcador.getYmin(), alto);
                    contenedor.addView(imagen, lista.size()-1,parametros);
                    lista.set(lista.size()-1,ma);
                    //recorrerMarcadores(marcador,temp,ubicacion,lista.size()-1);
                    while(temp.verificarColisionMarcador(marcador)||marcador.verificarColisionMarcador(temp)
                            ||temp2.verificarColisionMarcador(marcador)||marcador.verificarColisionMarcador(temp2)
                            ||temp.verificarColisionMarcador(ma)||ma.verificarColisionMarcador(temp)){
                        temp=lista.get(location);
                        marcador=lista.get(lista.size()-2);
                        //Log.d("Colision", "Colision"+String.valueOf(iii));
                        recorrerMarcadoresY(lista.size()-2,(int)((CajaDeTexto)this.contenedor.getChildAt(location)).getY());
                    }
                }
            }
        }
    }

    public void recorrerMarcadores(Marcador actual,Marcador anterior,Location ubicacion,int location){
        //Calculamos bearing de cada marcador
        double bearing_actual,bearing_anterior;
        bearing_actual=ubicacion.bearingTo(actual.getLocalizacionLugar());
        bearing_anterior=ubicacion.bearingTo(anterior.getLocalizacionLugar());
        if (bearing_actual<bearing_anterior||bearing_actual-bearing_anterior>Pantalla.Angulo_Tolerancia){
            //El marcador que se esta agregando esta a la izquierda
            recorrerIzquierda(location);
        }
        else{
            //El marcador que se esta agregando esta a la derecha
            recorrerDerecha(location);
        }
    }

    public void recorrerDerecha(int location){
        //Log.d("ContenedorCajas", String.valueOf(location));
        CajaDeTexto caja=(CajaDeTexto)this.contenedor.getChildAt(location);
        caja.setX(caja.getX()+30);

        RelativeLayout.LayoutParams parametros=new RelativeLayout.LayoutParams(
                ancho,
                alto);
        contenedor.removeViewAt(location);
        contenedor.addView(caja, location, parametros);

        Marcador m=lista.get(location);
        m.setValoresX((int) this.contenedor.getChildAt(location).getX(), ancho);
        //lista.add(1, m);
        lista.set(location, m);

        Button imagen=(Button)this.contenedor.getChildAt(location+1);
        imagen.setGravity(Gravity.CENTER);
        imagen.setEnabled(false);
        imagen.setX(imagen.getX() + 30);

        parametros=new RelativeLayout.LayoutParams(alto-20
                , alto);
        contenedor.removeViewAt(lista.size()-1);
        contenedor.addView(imagen, location+1,parametros);

        m=lista.get(location+1);
        m.setValoresX((int) this.contenedor.getChildAt(location+1).getX(), alto);
        lista.set(location+1,m);
        // marcador.setValoresX(x, (int) (CajaDeTexto.getAnchoGenerico(marcador.getTextoMasLargo())*Pantalla.DENSIDAD+5));
        // marcador.setValoresY(y, caja.obtenerAlto()*(marcador.getNumLineas()));

    }

    public void recorrerIzquierda(int location){
        //Log.d("ContenedorCajas", String.valueOf(location));
        CajaDeTexto caja=(CajaDeTexto)this.contenedor.getChildAt(location);
        caja.setX(caja.getX()-30);

        RelativeLayout.LayoutParams parametros=new RelativeLayout.LayoutParams(
                ancho,
                alto);
        contenedor.removeViewAt(location);
        contenedor.addView(caja, location, parametros);

        Marcador m=lista.get(location);
        m.setValoresX((int) this.contenedor.getChildAt(location).getX(), ancho);
        //lista.add(1, m);
        lista.set(location, m);

        Button imagen = (Button)this.contenedor.getChildAt(location + 1);
        imagen.setGravity(Gravity.CENTER);
        imagen.setEnabled(false);
        imagen.setX(imagen.getX() - 30);

        parametros=new RelativeLayout.LayoutParams(alto-20
                , alto);
        contenedor.removeViewAt(lista.size()-1);
        contenedor.addView(imagen, location+1,parametros);

        m=lista.get(location+ 1);
        m.setValoresX((int) this.contenedor.getChildAt(location + 1).getX(), alto);
        lista.set(location+1,m);
    }


    public void recorrerMarcadoresY(int location,int posY){
        //Calculamos bearing de cada marcador
        CajaDeTexto caja=(CajaDeTexto)this.contenedor.getChildAt(location);
        Button imagen=(Button)this.contenedor.getChildAt(location+1);
        imagen.setGravity(Gravity.CENTER);
        imagen.setEnabled(false);
        //posY=(int)caja.getY();
        if (posY>=2*Pantalla.ALTO/3){//Movemos arriba
            caja.setY(caja.getY()-10);
            imagen.setY(imagen.getY()-10);
            //Log.d("ContenedorCajas", "Arriba");
        }
        else{ //Movemos abajo
            caja.setY(caja.getY()+10);
            imagen.setY(imagen.getY()+10);
            //Log.d("ContenedorCajas", "Abajo");
        }
        RelativeLayout.LayoutParams parametros=new RelativeLayout.LayoutParams(
                ancho,
                alto);
        contenedor.removeViewAt(location);
        contenedor.addView(caja, location, parametros);
        Marcador m=lista.get(location);
        m.setValoresY((int) this.contenedor.getChildAt(location).getY(), alto);
        //lista.add(1, m);
        lista.set(location, m);

        parametros=new RelativeLayout.LayoutParams(alto-20
                ,alto);
        contenedor.removeViewAt(lista.size()-1);
        contenedor.addView(imagen, location+1,parametros);
        m=lista.get(location+1);
        m.setValoresY((int) this.contenedor.getChildAt(location + 1).getY(), alto);
        lista.set(location+1,m);
    }

    private static int countLines(String str){
        String[] lines = str.split("\r\n|\r|\n");
        return  lines.length;
    }

    public static int dpToPx(int dp)
    {
        return (int) (dp * Resources.getSystem().getDisplayMetrics().density);
    }



    public class AsyckTask extends AsyncTask<Void,Void,Void>{

        Map<String,String> oficina;
        LatLng position;
        String finalType;

        public AsyckTask(Map<String,String> oficina, LatLng position, String finalType){
            this.oficina = oficina;
            this.position = position;
            this.finalType = finalType;
        }

        @Override
        protected Void doInBackground(Void... params) {

            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            FragmentDialogOfficesMap dialogo = new FragmentDialogOfficesMap();
            dialogo.setList(oficina);
            dialogo.setLatLng(position);
            dialogo.setType(finalType);
            dialogo.show(fragmentManager, FragmentDialogOfficesMap.TAG);

            isDialogClosed = true;
            return null;
        }


    }


}
