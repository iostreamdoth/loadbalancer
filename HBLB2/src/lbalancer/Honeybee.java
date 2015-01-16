package lbalancer;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

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

public class Honeybee implements IHttpRequestHandler, ILifeCycle {
	private final List<InetSocketAddress> servers = new ArrayList<InetSocketAddress>();
	private HttpClient httpClient;
	private static List<ServerInfo> lstServer = new ArrayList<ServerInfo>();
	private static double dblTotalLoad = 0.0;
	private static double dblTotalCapacity = 0.0;
	private static double dblMaxCapacity = 0.0;
	private static double dblTotalProcessingTime;
	private static double dblStandardDeviation;
	private static double dblThreshhold;
	private static long lngCurrentTaskId;

	 public void onInit() {
	      httpClient = new HttpClient();
	      httpClient.setAutoHandleCookies(false);
	}


	   public void onDestroy() throws IOException {
	      httpClient.close();
	   }
	
	public Honeybee(InetSocketAddress... srvs) {
		servers.addAll(Arrays.asList(srvs));
		int count = 0;
		lngCurrentTaskId = 1;
		for (Iterator<InetSocketAddress> iter = servers.iterator(); iter
				.hasNext();) {
			InetSocketAddress oServer = iter.next();
			//lstServer.add(new ServerInfo(oServer, count,"zone"));
			count++;
		}
	}
	
	public static void setThreshold(double newThreshold) {
		dblThreshhold = newThreshold;
	}

	public static void addLoad(double newLoad) {
		dblTotalLoad += newLoad;
	}

	public static void HoneyBeeBalancing() {
		List<ServerInfo> lstOVM;
		List<ServerInfo> lstLVM;
		List<ServerInfo> lstBVM;
		calculateSupply();
		lstOVM = sortOVMS();
		lstLVM = sortLVMS();
		lstBVM = getBVMS();
		while (lstLVM.size() == 0 && lstOVM.size() == 0) {
			for (int s = 0; s <= lstOVM.size(); s++) {
				for (Iterator<TaskInfo> iter = lstOVM.get(s).lstTaskInfo
						.iterator(); iter.hasNext();) {
					TaskInfo oTaskInfo = iter.next();
					ServerInfo transServer = lstLVM.get(0);
					ServerInfo transFromServer = lstOVM.get(s);
					if (!oTaskInfo.isTaskPreemptive) {

						for (int x = 0; x < lstLVM.size(); x++) {
							int minHTask = Integer.MAX_VALUE;
							int minMTask = Integer.MAX_VALUE;
							int minLTask = Integer.MAX_VALUE;
							int intHtasks = 0;
							int intMtasks = 0;
							int intLtasks = 0;

							if (lstLVM.get(x).dblLoad <= lstLVM.get(x).dblCapacity) {
								for (Iterator<TaskInfo> tskIter = lstLVM.get(x).lstTaskInfo
										.iterator(); tskIter.hasNext();) {
									TaskInfo oIterTaskInfo = iter.next();

									if (oIterTaskInfo.intTaskPriority == 2) {
										intHtasks += 1;
									}
									if (oIterTaskInfo.intTaskPriority == 1) {
										intMtasks += 1;
									}
									if (oIterTaskInfo.intTaskPriority == 0) {
										intLtasks += 1;
									}
								}
								if (oTaskInfo.intTaskPriority == 2) {
									if (minHTask > intHtasks) {
										transServer = lstLVM.get(x);
										minHTask = intHtasks;
									}

								}
								if (oTaskInfo.intTaskPriority == 1) {
									if (minMTask > (intHtasks + intMtasks)) {
										transServer = lstLVM.get(x);
										minMTask = intHtasks + intMtasks;
									}

								}
								if (oTaskInfo.intTaskPriority == 0) {
									if (minLTask > (intHtasks + intMtasks + intLtasks)) {
										transServer = lstLVM.get(x);
										minLTask = intHtasks + intMtasks
												+ intLtasks;
									}
								}
							}
						}
					} else {

						for (int x = 0; x < lstLVM.size(); x++) {
							int minHTask = Integer.MAX_VALUE;
							int minMTask = Integer.MAX_VALUE;
							int minLTask = Integer.MAX_VALUE;
							int intHtasks = 0;
							int intMtasks = 0;
							int intLtasks = 0;

							for (Iterator<TaskInfo> tskIter = lstLVM.get(x).lstTaskInfo
									.iterator(); tskIter.hasNext();) {
								TaskInfo oIterTaskInfo = iter.next();

								if (oIterTaskInfo.intTaskPriority == 2) {
									intHtasks += 1;
								}
								if (oIterTaskInfo.intTaskPriority == 1) {
									intMtasks += 1;
								}
								if (oIterTaskInfo.intTaskPriority == 0) {
									intLtasks += 1;
								}
							}
							if (oTaskInfo.intTaskPriority == 2) {
								if (minHTask > intHtasks) {
									transServer = lstLVM.get(x);
									minHTask = intHtasks;
								}

							} else if (oTaskInfo.intTaskPriority == 1) {
								if (minMTask > (intHtasks + intMtasks)) {
									transServer = lstLVM.get(x);
									minMTask = intHtasks + intMtasks;
								}

							} else {
								if (minLTask > (intHtasks + intMtasks + intLtasks)) {
									transServer = lstLVM.get(x);
									minLTask = intHtasks + intMtasks
											+ intLtasks;
								}
							}
						}
					}
					taskTransfer(transFromServer, transServer, oTaskInfo);
					calculateSupply();
					lstOVM = sortOVMS();
					lstLVM = sortLVMS();
					lstBVM = getBVMS();
				}
			}
		}
	}

	public static void taskTransfer(ServerInfo oFromServer,
			ServerInfo oToServer, TaskInfo oTaskInfo) {
			int intKeepCount=0;
		for (Iterator<TaskInfo> iter = oToServer.lstTaskInfo.iterator(); iter.hasNext();) {
			TaskInfo oTask = iter.next();
			if(oTask.lngTaskId==oTaskInfo.lngTaskId)
			{
				oToServer.RemoveTask(intKeepCount);
				break;
			}
			intKeepCount++;
		}
		oToServer.AssignTask(oTaskInfo);
	}

	public static List<ServerInfo> sortOVMS() {
		List<ServerInfo> lstOVM = new ArrayList<ServerInfo>();
		for (Iterator<ServerInfo> iter = lstServer.iterator(); iter.hasNext();) {
			ServerInfo oServer = iter.next();
			if (oServer.dblServiceRate < 0) {
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
			if (oServer.dblServiceRate > 0) {
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
			if (oServer.dblServiceRate == 0) {
				lstBVM.add(oServer);
			}
		}
		return lstBVM;
	}

	public static void calculateTotalProcessingTime() {
		dblTotalProcessingTime = dblTotalLoad / dblTotalCapacity;
	}

	public static void calculateStandardDeviation() {
		double dblsum = 0.0;
		double dblDiff = 0.0;
		for (Iterator<ServerInfo> iter = lstServer.iterator(); iter.hasNext();) {
			ServerInfo oServer = iter.next();
			dblDiff = oServer.dblLoad - dblStandardDeviation;
			dblsum += dblDiff * dblDiff;
		}
		dblsum = dblsum / lstServer.size();
		dblsum = Math.sqrt(dblsum);
	}

	public static void addCapacity(double newCapacity) {
		dblTotalCapacity += newCapacity;
	}

	public static void calculateSupply() {
		for (Iterator<ServerInfo> iter = lstServer.iterator(); iter.hasNext();) {
			ServerInfo oServer = iter.next();
			oServer.calculateSupply();
		}
	}

	public static void removeLoad(double removeLoad) {
		dblTotalLoad -= removeLoad;
	}

	public static void removeCapacity(double removeCapacity) {
		dblTotalCapacity -= removeCapacity;
	}

	public void transferTask(final IHttpExchange exchange) throws IOException,
			BadMessageException {
		IHttpRequest request = exchange.getRequest();
		// Transfer Request method to be implemented after application
		// completion
	}

	

	@Override
	public void onRequest(final IHttpExchange exchange) throws IOException,
			BadMessageException {
		IHttpRequest request = exchange.getRequest();

		// Honey bee algorithm will be implemented here

		// Selection Process of algorithm
		InetSocketAddress server = servers.get(1);
		ServerInfo oServerInfo = lstServer.get(1);
		System.out.print(" 1 was here ");
		//calculateStandardDeviation();
		if (dblStandardDeviation > dblThreshhold) {
			if (dblTotalLoad < dblMaxCapacity) {
				//HoneyBeeBalancing();
			}
		}
		System.out.print(" 2 was here ");
		// Selection process ends here.
		lngCurrentTaskId++;
		TaskInfo oTask = new TaskInfo();
		oTask.intSeverID = oServerInfo.intServerID;
		oTask.lngTaskId = lngCurrentTaskId;
		oTask.exchange = exchange;
		oServerInfo.lstTaskInfo.add(oTask);
		// Algorithm to end here
		String strRequester = request.getHeader("User-Agent");
		URL url = request.getRequestUrl();
		URL newUrl = new URL(url.getProtocol(), server.getHostName(),
				server.getPort(),  url.getFile());
		request.removeHopByHopHeaders();
		request.addHeader("ServerID",Integer.toString(oServerInfo.intServerID));
		request.setRequestUrl(newUrl);
		System.out.print(request);
		
		
		System.out.print(request);
		System.out.print("\n");
		System.out.print(url.getProtocol());
		System.out.print("\n");
		System.out.print(server.getHostName());
		System.out.print("\n");
		System.out.print(server.getPort());
		System.out.print("\n");
		System.out.print(url.getFile());
		System.out.print("\n");
		System.out.print(newUrl);
		System.out.print("\n");
		System.out.print(" 3 was here ");
		// TODO Auto-generated method stub
		IHttpResponseHandler respHdl = new IHttpResponseHandler() {
			
			@Execution(Execution.NONTHREADED)
			public void onException(IOException ioe) throws IOException {
				exchange.sendError(ioe);
			}

			@Override
			public void onResponse(IHttpResponse response) throws IOException {
				// TODO Auto-generated method stub
				//exchange.send(new HttpResponse(200, "text/plain", "Show it"));
				
				exchange.send(response);
			}
		};
		System.out.print(" 4 was here ");
		 // forward the request in a asynchronous way by passing over the response handler
	     httpClient.send(request, respHdl);
	}
}
