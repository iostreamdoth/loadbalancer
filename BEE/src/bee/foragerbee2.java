package bee;

import java.io.IOException;
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


//import lbalancer.ServerInfo;
import bee.requestr;

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

public class foragerbee2 implements IHttpRequestHandler, ILifeCycle {
	private HttpClient httpClient;
	static long lngComputeResource;
	static long lngStorageResource;
	static long lngMemoryResource;
	public static int requestcount = 0;
	public LinkedList<IHttpExchange> rqstqueue = new LinkedList<IHttpExchange>();
	
	//private Lock lock;
	public void onInit() {
		httpClient = new HttpClient();
		httpClient.setAutoHandleCookies(false);
	}
	foragerbee2()
	{
		lngComputeResource=0;
		lngStorageResource=0;
		lngMemoryResource = 0;
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
								processNodeQueue();
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
	public void onDestroy() throws IOException {
		httpClient.close();
	}

	@Execution(Execution.MULTITHREADED)
	@Override
	public void onRequest(final IHttpExchange exchange) throws IOException,
			BadMessageException {
		synchronized (this) {
			rqstqueue.add(exchange);
			requestcount++;
		}
		/*IHttpRequest request = exchange.getRequest();
		final String strServerID = request.getHeader("ServerID");
		InetSocketAddress server = new InetSocketAddress("localhost", 8765);
		
		URL url = request.getRequestUrl();
		URL newUrl = new URL(url.getProtocol(), server.getHostName(),
				server.getPort(),  url.getFile());
		request.removeHopByHopHeaders();
		
		String rResource = request.getRequestURI();
		System.out.println("I came in forager beeeee");
		System.out.println(rResource);
		if(!rResource.equals("/")){
		String [] arrstr = rResource.split("/");
		//System.out.println(arrstr[arrstr.length-1].toLowerCase());
		rResource = arrstr[arrstr.length-1].toLowerCase();}
		else
		{
			rResource="/";
		}
		
		requestr oR = calculateConsumption(request.getMethod(),rResource);	
		//System.out.println("I came in forager beeeee before setting URL");
		request.setRequestUrl(newUrl);
		synchronized(this){
			//allocateResource(request);
			lngComputeResource+=oR.computecount;
			lngStorageResource+=oR.memorycount;
			lngMemoryResource += oR.storagecount;
			}
		
		
		
		final long lngStorageResourceToSend = lngComputeResource;
		final long lngMemoryResourceToSend = lngStorageResource;
		final long lngComputeResourceToSend=lngMemoryResource;

		System.out.println("I came in forager beeeee Till responseHandler");
		IHttpResponseHandler respHdl = new IHttpResponseHandler() {

			@Execution(Execution.NONTHREADED)
			public void onException(IOException ioe) throws IOException {
				exchange.sendError(ioe);
			}

			@Override
			public void onResponse(IHttpResponse response) throws IOException {
				// TODO Auto-generated method stub
				System.out.println(lngComputeResource);
				response.addHeader("Compute",Long.toString(lngComputeResourceToSend));
				response.addHeader("Memory",Long.toString(lngMemoryResourceToSend));
				response.addHeader("Storage",Long.toString(lngStorageResourceToSend));
				response.addHeader("ServerID",strServerID);
				exchange.send(response);
				
			}
		};
		httpClient.send(request, respHdl);
		synchronized(this){
		//deallocateResource(request);
			lngComputeResource-=oR.computecount;;
			lngStorageResource-=oR.memorycount;
			lngMemoryResource -= oR.storagecount;

		}*/
	}
////////////////////////////////////////////////////////////////
	
	//////////////////////////////////
	public void processNodeQueue() {
		final IHttpExchange exchange = rqstqueue.getFirst();
		IHttpRequest request = exchange.getRequest();
		String rType = request.getMethod();
		String rResource = request.getRequestURI();

		if (!rResource.equals("/")) {
			String[] arrstr = rResource.split("/");
			rResource = arrstr[arrstr.length - 1].toLowerCase();
		} else {

			rResource = "/";
		}
		requestr o = calculateConsumption(rType, rResource);
		final String strServerID = request.getHeader("ServerID");
		InetSocketAddress server = new InetSocketAddress("localhost", 8765);
		URL url = request.getRequestUrl();
		URL newUrl = null;
		try {
			newUrl = new URL(url.getProtocol(), server.getHostName(),
					server.getPort() , url.getFile());
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			System.out.println("Error URL");
			e.printStackTrace();
		}
		request.removeHopByHopHeaders();
		//request.addHeader("ServerID", Integer.toString(this.intServerID));
		request.setRequestUrl(newUrl);
		synchronized(this){
				lngComputeResource+=o.computecount;
				lngStorageResource+=o.memorycount;
				lngMemoryResource += o.storagecount;
			}

		final long lngStorageResourceToSend = lngComputeResource;
		final long lngMemoryResourceToSend = lngStorageResource;
		final long lngComputeResourceToSend=lngMemoryResource;

		
		IHttpResponseHandler respHdl = new IHttpResponseHandler() {

			@Execution(Execution.NONTHREADED)
			public void onException(IOException ioe) throws IOException {
				exchange.sendError(ioe);
			}

			
			@Execution(Execution.MULTITHREADED)
			@Override
			public void onResponse(IHttpResponse response) throws IOException {
				// TODO Auto-generated method stub
				System.out.println(lngComputeResource);
				response.addHeader("Compute",Long.toString(lngComputeResourceToSend));
				response.addHeader("Memory",Long.toString(lngMemoryResourceToSend));
				response.addHeader("Storage",Long.toString(lngStorageResourceToSend));
				response.addHeader("ServerID",strServerID);
				exchange.send(response);	
			}
		};
		//httpClient.send(request, respHdl);
		synchronized(this){
		//deallocateResource(request);
			lngComputeResource-=o.computecount;;
			lngStorageResource-=o.memorycount;
			lngMemoryResource -= o.storagecount;

		
		try {
			httpClient.send(request, respHdl);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println("Error SEND");
			e.printStackTrace();
		}
		
		/*
		 * System.out.println("Response:- " + this.strServerName + ":" +
		 * this.intPort.toString() + " ," + Integer.toString(this.intServerID) +
		 * " ," + Double.toString(this.dblSupplyVM) + " ," +
		 * Long.toString(this.lngComputeResource));
		 */
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
