package bee;

import bee.foargerbee;

import org.xlightweb.server.HttpServer;

public class runbee {
	public static void main(String[] args) throws Exception {
		HttpServer Server1 = new HttpServer(10089, new foargerbee());
		HttpServer Server2 = new HttpServer(10090, new foargerbee());
		HttpServer Server3 = new HttpServer(10091, new foargerbee());
		HttpServer Server4 = new HttpServer(10092, new foargerbee());
		HttpServer Server5 = new HttpServer(10093, new foargerbee());
		HttpServer Server6 = new HttpServer(10094, new foargerbee());
		HttpServer Server7 = new HttpServer(10095, new foargerbee());
		HttpServer Server8 = new HttpServer(10096, new foargerbee());
		HttpServer Server9 = new HttpServer(10097, new foargerbee());

		try {

			Server1.start();
			Server2.start();
			Server3.start();
			Server4.start();
			Server5.start();
			Server6.start();
			Server7.start();
			Server8.start();
			Server9.start();

		} catch (Exception ex) {

		}
	}
}
