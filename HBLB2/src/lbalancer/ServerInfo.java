package lbalancer;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

import org.xlightweb.BodyDataSource;
import org.xlightweb.IHttpExchange;
import org.xlightweb.IHttpRequest;
import org.xlightweb.IHttpResponse;
import org.xlightweb.IHttpResponseHandler;
import org.xlightweb.IPart;
import org.xlightweb.client.HttpClient;
import org.xsocket.Execution;

public class ServerInfo {

	public LinkedList<IHttpExchange> rqstqueue = new LinkedList<IHttpExchange>();

	private HttpClient httpClient;
	String strServerName;
	InetAddress inetServerAddress;
	double dblTasklengthAssigned;
	double dblProcessTime;
	double dblLoad;
	double dblCapacity;
	double dblMAXCapacity;
	List<String> taskDetails;
	double dblServiceRate;
	List<Integer> taskProcessTimes = new ArrayList<Integer>();
	Integer intPort;
	InetSocketAddress inetNode;
	String strServerType;
	List<TaskInfo> lstTaskInfo = new ArrayList<TaskInfo>();
	Integer intNumberOfTasks;
	double dblSupplyVM;
	Integer intPosition;
	Integer intNumberHTask;
	Integer intNumberMTask;
	Integer intNumberLTask;
	double dblNumberOfPrecessors;
	double dblMilInsPerSec;
	double dblBandWidth;
	int intServerID;
	public static int intServerIDCount = 0;

	long lngComputeResource;
	long lngStorageResource;
	long lngMemoryResource;

	long lngComputeCapacity;
	long lngStorageCapacity;
	long lngMemoryCapacity;

	long lngCurrentComputeResource;
	long lngCurrentStorageResource;
	long lngCurrentMemoryResource;
	long lngThreshHold1;
	long lngThreshHold2;
	boolean isCommissioned;
	// int requestcount = 0;
	Zone oZone;

	List<Zone> lstReqFromZone = new ArrayList<Zone>();

	public int requestcount = 0;
	public int memorycount = 0;
	public int computecount = 0;
	public int storagecount = 0;

	public ServerInfo(InetSocketAddress cloudNode, String zoneid) {

		oZone = Zone.getZoneObject(zoneid);
		System.out.println("This is the constructor which is called Inetsocketaddress");
		//System.out.println(zoneid);
		//System.out.println(oZone.zone);
		for (Zone o : Zone.lstZone) {
			lstReqFromZone.add(new Zone(o.latitude, o.longitude, o.zone));
		}

		int intp = cloudNode.getPort();
		strServerName = cloudNode.getHostName();
		inetServerAddress = cloudNode.getAddress();
		intPort = cloudNode.getPort();
		inetNode = cloudNode;
		intPosition = intp;
		synchronized (this) {
			intServerID = intServerIDCount++;
			oZone.serverCount++;
			//System.out.println("Adding server to the class zone of " +oZone.zone);
			oZone.lstServerUnderZone.add(this);
			System.out.println("Adding server to the size of server is at " +Zone.lstZone.get(0).lstServerUnderZone.size());
			
		}
		// Default Values for the VM's.
		dblServiceRate = 5;
		dblNumberOfPrecessors = 4;
		dblMilInsPerSec = 5;
		dblBandWidth = 5;

		lngComputeResource = 0;
		lngStorageResource = 0;
		lngMemoryResource = 0;

		lngCurrentComputeResource = 0;
		lngCurrentStorageResource = 0;
		lngCurrentMemoryResource = 0;

		lngComputeCapacity = 3000000;
		lngStorageCapacity = 900000000;
		lngMemoryCapacity = 102400000;
		isCommissioned = false;
		lngThreshHold1 = (lngComputeCapacity * 90) / 100;
		lngThreshHold2 = (lngComputeCapacity * 98) / 100;
		httpClient = new HttpClient();
		httpClient.setAutoHandleCookies(false);
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

								processNodeQueue();
								rqstqueue.removeFirst();
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

	public ServerInfo(InetSocketAddress cloudNode, int intp) {
		oZone = new Zone();
		strServerName = cloudNode.getHostName();
		inetServerAddress = cloudNode.getAddress();
		intPort = cloudNode.getPort();
		inetNode = cloudNode;
		intPosition = intp;
		synchronized (this) {
			intServerID = intServerIDCount++;
		}
		// Default Values for the VM's.
		dblServiceRate = 5;
		dblNumberOfPrecessors = 4;
		dblMilInsPerSec = 5;
		dblBandWidth = 5;

		lngComputeResource = 0;
		lngStorageResource = 0;
		lngMemoryResource = 0;

		lngCurrentComputeResource = 0;
		lngCurrentStorageResource = 0;
		lngCurrentMemoryResource = 0;

		lngComputeCapacity = 3000000;
		lngStorageCapacity = 900000000;
		lngMemoryCapacity = 102400000;
		isCommissioned = false;
		lngThreshHold1 = (lngComputeCapacity * 90) / 100;
		lngThreshHold2 = (lngComputeCapacity * 98) / 100;
		httpClient = new HttpClient();
		httpClient.setAutoHandleCookies(false);
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
								// System.out.println("Oh I entered in thread of server "
								// + strServerName + ":" + intPort);
								// System.out.println("Total Size  of queue of node "+strServerName
								// + " " + intPort + " = " + rqstqueue.size());
								processNodeQueue();
								rqstqueue.removeFirst();
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

	public void finalize() throws Throwable {
		httpClient.close();
	}

	public void AssignTask(TaskInfo oTask) {
		lstTaskInfo.add(oTask);
		dblTasklengthAssigned++;
	}

	public void RemoveTask(int index) {
		lstTaskInfo.remove(index);
		dblTasklengthAssigned--;
	}

	public double calculateLoad() {
		dblLoad = dblTasklengthAssigned / dblServiceRate;
		return dblLoad;
	}

	public void calculateSupply() {
		dblSupplyVM = dblMAXCapacity - dblLoad / dblCapacity;
	}

	public void calculateCapacity() {
		dblMAXCapacity = (dblNumberOfPrecessors * dblMilInsPerSec)
				+ dblBandWidth;
	}

	public void sortTaskPriority() {

	}

	public void processNodeQueue() {
		final IHttpExchange exchange = rqstqueue.getFirst();
		IHttpRequest request = exchange.getRequest();

		// System.out.println(request);
		//System.out.println("In processNodeQueue");

		// List<IPart> parts = request.getBody().readParts();
		// String bodystring = bodyDataSource.toString();
		// System.out.println(parts.toString());
		// string lat = bodystring.indexOf("hdnLatitude=")
		double lat;
		double lng;
		try {
			lat = Double.parseDouble(request.getParameter("hdnLatitude"));
			lng = Double.parseDouble(request.getParameter("hdnLongitude"));
		} catch (Exception ex) {
			lat = 0.0;
			lng = 0.0;
		}

		//System.out.println("About to print something");
		// System.out.println();
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
		requestr o = calculateConsumption(rType, rResource);
		// Append this request to the particulr zone
		assignZoneToRequest(lat, lng, o);
		URL url = request.getRequestUrl();
		URL newUrl = null;
		try {
			newUrl = new URL(url.getProtocol(), this.strServerName,
					this.intPort, url.getFile());
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			System.out.println("Error URL");
			e.printStackTrace();
		}
		request.removeHopByHopHeaders();
		request.addHeader("ServerID", Integer.toString(this.intServerID));
		request.setRequestUrl(newUrl);
		final ServerInfo os = this;
		//System.out.println(newUrl);
		//System.out.println(request);
		IHttpResponseHandler respHdl = new IHttpResponseHandler() {

			@Execution(Execution.NONTHREADED)
			public void onException(IOException ioe) throws IOException {
				exchange.sendError(ioe);
				System.out.println(ioe.getMessage());
			}

			@Override
			public void onResponse(IHttpResponse response) throws IOException {

				//System.out.println("Is error here");

				String strCompute = response.getHeader("Compute");
				String strMemory = response.getHeader("Memory");
				// System.out.println("Is error here1");
				String strStorage = response.getHeader("Storage");
				// System.out.println("Is error here2");
				String strServerID = response.getHeader("ServerID");
				// System.out.println("Is error here3");
				os.lngCurrentComputeResource = Long.parseLong(strCompute);
				os.lngCurrentMemoryResource = Long.parseLong(strMemory);
				os.lngCurrentStorageResource = Long.parseLong(strStorage);
				exchange.send(response);

			}
		};
		try {
			httpClient.send(request, respHdl);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			//System.out.println("Error SEND");
			e.printStackTrace();
		}
		//System.out.println("About to print something2");
		synchronized (this) {
			this.lngComputeResource -= o.computecount;
			this.lngMemoryResource -= o.memorycount;
			this.lngStorageResource -= o.storagecount;
		}
		/*
		 * System.out.println("Response:- " + this.strServerName + ":" +
		 * this.intPort.toString() + " ," + Integer.toString(this.intServerID) +
		 * " ," + Double.toString(this.dblSupplyVM) + " ," +
		 * Long.toString(this.lngComputeResource));
		 */

	}

	public void assignZoneToRequest(double lat, double lng, requestr oR) {
		Zone o = Zone.getNearestZone(lat, lng);
		synchronized (lstReqFromZone) {
		for (Zone oZone : lstReqFromZone) {
				if (oZone.zone == o.zone) {
					oZone.computecount += oR.computecount;
					oZone.memorycount += oR.memorycount;
					oZone.storagecount += oR.storagecount;
					oZone.requestcount += oR.requestcount;

				}
			}
		}
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
