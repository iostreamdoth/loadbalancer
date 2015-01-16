package lbalancer;

import java.net.InetSocketAddress;

import org.xlightweb.server.HttpServer;

public class Balancer {
	public static void main(String[] args) throws Exception {
		ServerInfo[] srvs = new ServerInfo[] {
				new ServerInfo(new InetSocketAddress("localhost", 10089),
						"zone1"),
				new ServerInfo(new InetSocketAddress("localhost", 10090),
						"zone2"),
				new ServerInfo(new InetSocketAddress("localhost", 10091),
						"zone3"),
				new ServerInfo(new InetSocketAddress("localhost", 10092),
						"zone4"),
				new ServerInfo(new InetSocketAddress("localhost", 10093),
						"zone5"),
				new ServerInfo(new InetSocketAddress("localhost", 10094),
						"zone6"),
				new ServerInfo(new InetSocketAddress("localhost", 10095),
						"zone7"),
				new ServerInfo(new InetSocketAddress("localhost", 10096),
						"zone8"),
				new ServerInfo(new InetSocketAddress("localhost", 10097),
						"zone9") };

		HBLB.algoType = "LA";
		HttpServer loadBalancer = new HttpServer(9090, new HBLB(srvs));
		try {
			statstodb b = new statstodb();
			new Thread(b).start();
			loadBalancer.run();

		} catch (Exception ex) {

		} finally {
			if (loadBalancer.isOpen()) {
				loadBalancer.close();
			}
		}

	}
}