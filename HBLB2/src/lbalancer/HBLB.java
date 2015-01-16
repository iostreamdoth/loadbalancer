package lbalancer;

import java.io.IOException;
import java.net.ConnectException;
import java.net.InetSocketAddress;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.xlightweb.BadMessageException;
import org.xlightweb.HttpResponse;
import org.xlightweb.IHttpExchange;
import org.xlightweb.IHttpRequest;
import org.xlightweb.IHttpRequestHandler;
import org.xlightweb.IHttpResponse;
import org.xlightweb.IHttpResponseHandler;
import org.xlightweb.client.HttpClient;
import org.xsocket.Execution;
import org.xsocket.ILifeCycle;

import java.sql.CallableStatement;
import java.sql.Statement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

public class HBLB implements IHttpRequestHandler, ILifeCycle {
	private final List<InetSocketAddress> servers = new ArrayList<InetSocketAddress>();
	private HttpClient httpClient;
	public static List<ServerInfo> lstServer = new ArrayList<ServerInfo>();
	public static List<ServerInfo> lstComissionedServer = new ArrayList<ServerInfo>();
	public static List<ServerInfo> lstLVM = new ArrayList<ServerInfo>();
	private static int intNextServerToComission = 0;
	private Lock lock;
	public static int requestcount = 0;
	public static int memorycount = 0;
	public static int computecount = 0;
	public static int storagecount = 0;
	public static String algoType = "HB";
	public static LinkedList<IHttpExchange> rqstqueue = new LinkedList<IHttpExchange>();
	public static String zonetobecomissioned = "zone1";

	public void onInit() {
		httpClient = new HttpClient();
		httpClient.setAutoHandleCookies(false);
	}

	public void onDestroy() throws IOException {
		httpClient.close();
	}

	public void runthistokeeptrack() {
		// your code here

		Connection conn = null;
		Statement st = null;
		ResultSet rs = null;
		try {
			// System.out.println("This one is fine 1");
			conn = DriverManager
					.getConnection("jdbc:mysql://localhost/resultview?"
							+ "user=tester&password=tester");
			st = conn.createStatement();
			// /rs = st.executeQuery("SELECT VERSION()");
			CallableStatement cStmt = conn
					.prepareCall("{call spgetset_loadbalancer(?, ?,?,?,?)}");
			// System.out.println("This one is fine 2");
			synchronized (this) {

				cStmt.setInt(1, requestcount);
				cStmt.setString(2, "HB");
				cStmt.setInt(3, memorycount);
				cStmt.setInt(4, storagecount);
				cStmt.setInt(5, computecount);
				// System.out.println("This one is fine 3");
				boolean hadResults = cStmt.execute();
				requestcount = 0;
				memorycount = 0;
				storagecount = 0;
				computecount = 0;
			}
			// System.out.println("This one is fine 4");

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public HBLB(InetSocketAddress... srvs) {
		servers.addAll(Arrays.asList(srvs));
		int count = 0;
		for (Iterator<InetSocketAddress> iter = servers.iterator(); iter
				.hasNext();) {
			InetSocketAddress oServer = iter.next();
			lstServer.add(new ServerInfo(oServer, count));
			count++;
		}
		lstServer.get(0).isCommissioned = true;
		intNextServerToComission = 1;

		Thread t1 = new Thread(new Runnable() {
			public void run() {
				while (true) {
					try {
						int size;
						synchronized (this) {
							// System.out.println("Oh I entered in thread sync");
							size = rqstqueue.size();
						}
						Thread.sleep(10);
						if (size != 0) {
							while (size != 0) {
								// System.out.println("Oh I entered in thread");
								processQueue();
								size = rqstqueue.size();
							}
						}
					} catch (Exception ex) {

					}
				}
			}
		});
		t1.start();

	}

	public HBLB() {

	}

	public HBLB(ServerInfo... srvs) {

		System.out.println("Starting HBLB SERVER");
		lstServer.addAll(Arrays.asList(srvs));
		int count = 0;
		for (Iterator<ServerInfo> iter = lstServer.iterator(); iter.hasNext();) {
			ServerInfo oServer = iter.next();
			servers.add(new InetSocketAddress(oServer.strServerName,
					oServer.intPort));
			count++;
		}
		if (!algoType.equals("AC") && !algoType.equals("LA")) {
			lstServer.get(0).isCommissioned = true;
			lstComissionedServer.add(lstServer.get(0));
			intNextServerToComission = 1;
		} else {
			lstComissionedServer = lstServer;
			for (ServerInfo o : lstComissionedServer) {
				o.isCommissioned = true;
			}
		}
		Thread t1 = new Thread(new Runnable() {
			public void run() {
				while (true) {
					try {
						int size;
						synchronized (this) {
							// System.out.println("Oh I entered in thread sync");
							size = rqstqueue.size();
						}
						Thread.sleep(10);
						if (size != 0) {
							while (size != 0) {
								// System.out.println("Oh I entered in thread");
								processQueue();
								size = rqstqueue.size();
							}
						}
					} catch (Exception ex) {

					}
				}
			}
		});
		t1.start();

	}

	public void processQueue() {
		// System.out.println("Oh I m in process queue");
		final IHttpExchange exchange = rqstqueue.getFirst();
		// System.out.println("Here 1");
		IHttpRequest request = exchange.getRequest();
		// System.out.println("Here 2");
		// System.out.println("Oh I entered and comissioned server length = " +
		// lstComissionedServer.size());
		// System.out.println("Here 3");
		// code goes here.
		// rqstqueue.removeFirst();
		int intServerId = 0;

		// System.out.println("Hello Ji");
		if (algoType == "HB") {
			intServerId = initiateBalance();
		}// Condition for swarm balancer.
		else if (algoType == "SW") {
			intServerId = SwarmBalancer();
		} else if (algoType == "AC") {
			double lat;
			double lng;
			try {
				lat = Double.parseDouble(request.getParameter("hdnLatitude"));
				lng = Double.parseDouble(request.getParameter("hdnLongitude"));
			} catch (Exception ex) {
				lat = 0.0;
				lng = 0.0;
			}
			intServerId = AntColony(lat, lng);
		} else {
			double lat;
			double lng;
			try {
				lat = Double.parseDouble(request.getParameter("hdnLatitude"));
				lng = Double.parseDouble(request.getParameter("hdnLongitude"));
			} catch (Exception ex) {
				lat = 0.0;
				lng = 0.0;
			}
			intServerId = LocationAware(lat, lng);
		}
		// System.out.println("Hello Ji1");
		InetSocketAddress server = servers.get(0);
		// System.out.println(lstComissionedServer.size());
		ServerInfo oServerInfo = null;
		for (ServerInfo o : lstComissionedServer) {
			if (o.intServerID == intServerId) {
				oServerInfo = o;
			}

		}
		if (oServerInfo == null) {
			oServerInfo = lstComissionedServer.get(0);

		}
		oServerInfo.rqstqueue.add(exchange);
		rqstqueue.removeFirst();
		String rType = request.getMethod();
		String rResource = request.getRequestURI();

		if (!rResource.equals("/")) {
			// System.out.println(rResource.length());
			String[] arrstr = rResource.split("/");

			rResource = arrstr[arrstr.length - 1].toLowerCase();

		} else {
			// System.out.println("Here");

			rResource = "/";
		}
		requestr oR = calculateConsumption(rType, rResource);

		synchronized (this) {
			oServerInfo.lngComputeResource += oR.computecount;
			oServerInfo.lngMemoryResource += oR.memorycount;
			oServerInfo.lngStorageResource += oR.storagecount;
			memorycount += oR.memorycount;
			computecount += oR.computecount;
			storagecount += oR.storagecount;

			oServerInfo.memorycount += oR.memorycount;
			oServerInfo.computecount += oR.computecount;
			oServerInfo.storagecount += oR.storagecount;

			oServerInfo.requestcount++;
			requestcount++;
		}

	}

	public int initiateBalance() {
		int intServerID = 0;
		// List<ServerInfo> lstOVM;
		List<ServerInfo> lstLVM;
		// List<ServerInfo> lstBVM;

		// lstOVM = sortOVMS();
		lstLVM = sortLVMList();
		// lstBVM = getBVMS();

		intServerID = lstLVM.get(0).intServerID;

		return intServerID;
	}

	public static List<ServerInfo> sortOVMS() {
		List<ServerInfo> lstOVM = new ArrayList<ServerInfo>();
		for (Iterator<ServerInfo> iter = lstServer.iterator(); iter.hasNext();) {
			ServerInfo oServer = iter.next();
			oServer.dblSupplyVM = oServer.lngComputeCapacity
					- oServer.lngCurrentComputeResource;
			if (oServer.dblSupplyVM > 300 && oServer.isCommissioned == true) {
				lstOVM.add(oServer);
			}
		}

		// Sorting in descending order
		Collections.sort(lstOVM, new Comparator<ServerInfo>() {
			public int compare(ServerInfo s1, ServerInfo s2) {
				if (s1.dblSupplyVM > s2.dblSupplyVM) {
					return -1;
				} else if (s1.dblSupplyVM < s2.dblSupplyVM)
					return 1;
				else
					return 0;

			}
		});
		return lstOVM;
	}

	public static List<ServerInfo> sortLVMS() {
		List<ServerInfo> lstLVM = new ArrayList<ServerInfo>();
		for (Iterator<ServerInfo> iter = lstServer.iterator(); iter.hasNext();) {
			ServerInfo oServer = iter.next();
			oServer.dblSupplyVM = oServer.lngComputeCapacity
					- oServer.lngCurrentComputeResource;
			if (oServer.dblSupplyVM < 300 && oServer.isCommissioned == true) {
				lstLVM.add(oServer);
			}
		}
		// Sorting in ascending order
		Collections.sort(lstLVM, new Comparator<ServerInfo>() {
			public int compare(ServerInfo s1, ServerInfo s2) {
				if (s1.dblSupplyVM > s2.dblSupplyVM) {
					return 1;
				} else if (s1.dblSupplyVM < s2.dblSupplyVM)
					return -1;
				else
					return 0;

			}
		});
		return lstLVM;
	}

	public static List<ServerInfo> getBVMS() {
		List<ServerInfo> lstBVM = new ArrayList<ServerInfo>();
		for (Iterator<ServerInfo> iter = lstServer.iterator(); iter.hasNext();) {
			ServerInfo oServer = iter.next();

			oServer.dblSupplyVM = oServer.lngComputeCapacity
					- oServer.lngCurrentComputeResource;
			if (oServer.dblSupplyVM == 300 && oServer.isCommissioned == true) {
				lstBVM.add(oServer);
			}

		}
		return lstBVM;
	}

	public int SwarmBalancer() {
		int temp = 0;
		// requestcount++;
		// TaskInfo t = new TaskInfo();
		// computecount = t.intProcessingTime;
		// memorycount = t.memory;
		// storagecount = t.storage;
		// List<ServerInfo> lstLVM = new ArrayList<ServerInfo>();
		for (Iterator<ServerInfo> iter = lstComissionedServer.iterator(); iter
				.hasNext();) {
			ServerInfo oServer = iter.next();
			if (oServer.lngComputeCapacity > computecount
					&& oServer.lngMemoryCapacity > memorycount
					&& oServer.lngStorageCapacity > storagecount) {
				lstLVM.add(oServer);
			}
		}

		for (int i = 0; i < lstComissionedServer.size() - 1; i++) {
			if (lstComissionedServer.get(i).lngComputeCapacity > lstComissionedServer
					.get(i + 1).lngComputeCapacity) {
				temp = i + 1;

			}
		}
		int id = lstComissionedServer.get(temp).intServerID;
		return id;
	}

	public int AntColony(double lat, double lng) {
		int intServerID = 0;

		intServerID = Zone.getNearestZone(lat, lng).lstServerUnderZone.get(0).intServerID;
		System.out.println(intServerID + "," + lstComissionedServer.size());
		return intServerID;

	}
	
	public int LocationAware(double lat, double lng) {
		int intServerID = 0;

		intServerID = Zone.getNearestZone(lat, lng).lstServerUnderZone.get(0).intServerID;
		System.out.println(intServerID + "," + lstComissionedServer.size());
		return intServerID;

	}

	public static List<ServerInfo> sortLVMList() {
		List<ServerInfo> lstLVM = new ArrayList<ServerInfo>();

		System.out.println("Start" + lstComissionedServer.size());
		Lock lock = new ReentrantLock();
		lock.lock();
		lstComissionedServer.clear();
		for (ServerInfo oServer : lstServer) {
			if (oServer.isCommissioned) {
				lstComissionedServer.add(oServer);
			}
		}

		for (Iterator<ServerInfo> iter = lstComissionedServer.iterator(); iter
				.hasNext();) {
			ServerInfo oServer = iter.next();

			oServer.dblSupplyVM = oServer.lngComputeCapacity
					- oServer.lngComputeResource;
		}
		lock.unlock();

		System.out.println("Stop");
		// System.out.println(lstLVM.get(0).strServerName);
		// Sorting in ascending order
		Collections.sort(lstComissionedServer, new Comparator<ServerInfo>() {
			public int compare(ServerInfo s1, ServerInfo s2) {
				if (s1.dblSupplyVM < s2.dblSupplyVM) {
					return 1;
				} else
					// if (s1.dblSupplyVM > s2.dblSupplyVM)
					return -1;
				// else
				// return -1;

			}
		});
		// System.out.println(lstServer);
		return lstComissionedServer;
	}

	@Execution(Execution.MULTITHREADED)
	@Override
	public void onRequest(final IHttpExchange exchange) throws IOException,
			BadMessageException {
		// IHttpRequest request = exchange.getRequest();
		synchronized (this) {
			rqstqueue.add(exchange);
			requestcount++;
		}
		// System.out.println("Total Size  of queue = " + rqstqueue.size());
	}

	public void performallocation(ServerInfo oServerInfo, requestr oR) {
		oServerInfo.lngComputeResource += oR.computecount;
		oServerInfo.lngMemoryResource += oR.memorycount;
		oServerInfo.lngStorageResource += oR.storagecount;
		memorycount += oR.memorycount;
		computecount += oR.computecount;

		requestcount++;
	}

	public void performdeallocation(ServerInfo oServerInfo, requestr oR) {
		oServerInfo.lngComputeResource -= oR.computecount;
		oServerInfo.lngMemoryResource -= oR.memorycount;
		oServerInfo.lngStorageResource -= oR.storagecount;

	}

	public requestr calculateConsumption(String rtype, String resource) {
		requestr o = new requestr();
		o.requestcount = 1;
		o.memorycount = 1;
		o.computecount = 1;
		o.storagecount = 1;
		if (rtype == "GET") {
			switch (resource) {
			case "index.aspx":
				o.requestcount = 1;
				o.memorycount = 2;
				o.computecount = 4;
				o.storagecount = 0;
				break;
			case "/":
				o.requestcount = 1;
				o.memorycount = 2;
				o.computecount = 4;
				o.storagecount = 0;
				break;
			case "bootstrap.css":
				o.requestcount = 1;
				o.memorycount = 2;
				o.computecount = 2;
				o.storagecount = 0;
				break;
			case "application.css":
				o.requestcount = 1;
				o.memorycount = 2;
				o.computecount = 2;
				o.storagecount = 0;
				break;
			case "jquery.min.js":
				o.requestcount = 1;
				o.memorycount = 2;
				o.computecount = 2;
				o.storagecount = 0;
				break;
			case "bootstrap.min.js":
				o.requestcount = 1;
				o.memorycount = 2;
				o.computecount = 2;
				o.storagecount = 0;
				break;
			}
		}
		if (rtype == "POST") {
			switch (resource) {
			case "viewresult.aspx":
				o.requestcount = 1;
				o.memorycount = 3;
				o.computecount = 10;
				o.storagecount = 2;
				break;
			case "upload.aspx":
				o.requestcount = 1;
				o.memorycount = 40;
				o.computecount = 80;
				o.storagecount = 20;
				break;

			}

		}
		return o;
	}

}
