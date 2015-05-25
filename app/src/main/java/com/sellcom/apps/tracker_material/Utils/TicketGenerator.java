package com.sellcom.apps.tracker_material.Utils;

import android.os.Looper;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.zebra.sdk.comm.BluetoothConnectionInsecure;
import com.zebra.sdk.comm.Connection;

public class TicketGenerator {

    static final String PRINTER_MAC_ADDRESS    = "AC:3F:A4:11:B2:B3";

    public static void generateTicket(List<Map<String,String>> products_info, Map<String,String> costumer_info, Map<String,String> salesman_info){
        new Thread(new Runnable() {
            public void run() {
                try {
                    Connection thePrinterConn = new BluetoothConnectionInsecure(PRINTER_MAC_ADDRESS);
                    Looper.prepare();
                    thePrinterConn.open();

                    // HEADER DATA (FIXED)
                    StringBuilder printData = new StringBuilder("! 0 200 200 1000 1");
                    printData.append("CENTER\r\n");
                    printData.append("SETBOLD 2\r\n");
                    printData.append("ENCODING UTF-8\r\n");
                    printData.append("T 7 1 0 80 RAGASA INDUSTRIAS SA DE CV\r\n");
                    printData.append("SETBOLD 0\r\n");
                    printData.append("T 7 0 0 135 RIN830930A79\r\n");
                    printData.append("T 7 0 0 165 DR JOSE ELEUTERIO GONZALEZ 2815\r\n");
                    printData.append("T 7 0 0 195 COL MITRAS CENTRO MONTERREY N.L. C.P. 64320\r\n");
                    printData.append("SETBOLD 2\r\n");
                    printData.append("T 7 0 0 225 SUCURSAL PUEBLA\r\n");
                    printData.append("SETBOLD 0\r\n");
                    printData.append("T 7 0 0 255 CALLE #5 No. 1306 COL REFORMA SUR PUEBLA, PUEBLA\r\n");
                    printData.append("T 7 0 0 285 01 800 NUTRIOLI\r\n");

                    printData.append("LEFT\r\n");
                    printData.append("T 7 0 0 315 \t\t" + DatesHelper.getStringDate(new Date())+"\r\n");
                    printData.append("RIGHT\r\n");
                    printData.append("T 7 0 0 315 "+"DH PUE 001\t\r\n");
                    printData.append("LEFT\r\n");
                    printData.append("T 7 0 0 345 "+"  RUTA       VENDEDOR\r\n");
                    printData.append("RIGHT\r\n");
                    printData.append("T 7 0 0 345 "+"NOMINA\t\r\n");
                    printData.append("LEFT\r\n");
                    printData.append("T 7 0 0 375 "+"   1     MARTIN MARTINEZ RAMIREZ\r\n");
                    printData.append("RIGHT\r\n");
                    printData.append("T 7 0 0 375 "+"25789\t\r\n");
                    printData.append("CENTER\r\n");
                    printData.append("T 7 0 0 405 www.ragasa.com.mx\r\n");

                    printData.append("LEFT\r\n");
                    printData.append("SETBOLD 2\r\n");
                    printData.append("T 7 0 0 445 \t\t\t\t\t\t\tVENTA\r\n");

                    printData.append("T 7 0 0 475 \tCOD.\t\tARTICULO\t\t\t\tCANT.\t\tPRE.UNIT\t\t\t\tTOTAL\r\n");
                    printData.append("PRINT\r\n");

                    int offset = 475;       // offset for ticket header

                    thePrinterConn.write(String.valueOf(printData).getBytes());

                    // Make sure the data got to the printer before closing the connection
                    Thread.sleep(500);
//                    myBitmap.recycle();
                    // Close the insecure connection to release resources.
                    thePrinterConn.close();
                    Looper.myLooper().quit();

                } catch (Exception e) {
                    // Handle communications error here.
                    e.printStackTrace();

                }
            }
        }).start();

    }
}
