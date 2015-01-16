package lbalancer;

import java.io.Console;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class statstodb implements Runnable {

	public static int requestcount = 0;
	public static int memorycount = 0;
	public static int computecount = 0;
	public static int storagecount = 0;

	@Override
	public void run() {
		while (true) {
			// HBLB o =null;
			try {
				Thread.sleep(5000);
			} catch (Exception ex) {
			}
			runthistokeeptrack();

			// System.out.println("Hello");
		}
	}

	public void runthistokeeptrack() {
		// your code here
		Connection conn = null;
		Statement st = null;
		ResultSet rs = null;
		// System.out.println("Hello1");
		try {
			// System.out.println("This one is fine 1");
			conn = DriverManager
					.getConnection("jdbc:mysql://localhost/resultview?"
							+ "user=tester&password=tester");
			st = conn.createStatement();
			// /rs = st.executeQuery("SELECT VERSION()");
			CallableStatement cStmt = conn
					.prepareCall("{call spgetset_loadbalancer(?, ?,?,?,?,?)}");
			// System.out.println("This one is fine 2");
			synchronized (this) {

				cStmt.setInt(1, HBLB.requestcount);
				cStmt.setString(2, HBLB.algoType);
				cStmt.setInt(3, HBLB.memorycount);
				cStmt.setInt(4, HBLB.storagecount);
				cStmt.setInt(5, HBLB.computecount);
				cStmt.setString(6, "I");
				// System.out.println("This one is fine 3");
				boolean hadResults = cStmt.execute();
				HBLB.requestcount = 0;
				HBLB.memorycount = 0;
				HBLB.storagecount = 0;
				HBLB.computecount = 0;

			}
			if (!conn.isClosed()) {
				conn.close();
			}
			// System.out.println("Hello Zone track");

			for (int i = 0; i < HBLB.lstServer.size(); i++) {
				runthistokeepzonetrack(HBLB.lstServer.get(i));
			}
			try {
				// System.out.println("Process Zone Server Info");
				processZoneServerInfo();

			} catch (Exception e) {
				// TODO Auto-generated catch block
				// System.out.println("Hello Error");
				e.printStackTrace();
			}
			// System.out.println("This one is fine 4");

		} catch (Exception e) {
			// System.out.println("Hello Big Error");
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void processZoneServerInfo() {
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
					.prepareCall("{call spgetset_zloadbalancer(?,?,?,?,?,?,?,?)}");
			// System.out.println("This one is fine 2");

			cStmt.setInt(1, 0);
			cStmt.setString(2, "");
			cStmt.setInt(3, 0);
			cStmt.setInt(4, 0);
			cStmt.setInt(5, 0);
			cStmt.setString(6, "");
			cStmt.setInt(7, 0);
			cStmt.setString(8, "ZT");

			// System.out.println("This one is fine 3");
			ResultSet result = cStmt.executeQuery();
			// System.out.println("This one is fine 4");
			List<Zone> lstZone = new ArrayList<Zone>();
			int mainrequestcount = 0;
			int mainmemorycount = 0;
			int maincomputecount = 0;
			int mainstoragecount = 0;
			// System.out.println("This one is fine 5");
			int maxtime = 0;
			int timeelapsed;

			while (result.next()) {
				Zone oZone = new Zone();
				// System.out.println("Result");
				oZone.computecount = result.getInt("computetime");
				// System.out.println("computetime");
				oZone.requestcount = result.getInt("requesttotal");
				// System.out.println("requesttotal");
				oZone.memorycount = result.getInt("memory");
				// System.out.println("memory");
				oZone.storagecount = result.getInt("storage");
				// System.out.println("storage");
				oZone.zone = result.getString("zone");
				// System.out.println("Result get");
				mainstoragecount += oZone.storagecount;
				maincomputecount += oZone.computecount;
				mainrequestcount += oZone.requestcount;
				mainmemorycount += oZone.memorycount;
				timeelapsed = result.getInt("minutedifference");
				if (maxtime < timeelapsed) {
					maxtime = timeelapsed;
				}
				// System.out.println("Result add");
				lstZone.add(oZone);
				// System.out.println("Result Last");
			}
			// System.out.println("This one is fine before collection");
			// For optimization and cost effectiveness
			Collections.sort(lstZone, new Comparator<Zone>() {

				@Override
				public int compare(Zone arg0, Zone arg1) {
					if (arg0.computecount < arg1.computecount) {
						return 1;
					} else if (arg0.computecount > arg1.computecount) {
						return -1;
					} else {
						return 0;

					}
				}
			});

			// synchronized (System.out) {
			// System.out.println("Maximum Time: " + maxtime);
			// System.out.flush();
			// }

			if (maxtime >= 60) {

				int intComissionedServers = HBLB.lstComissionedServer.size();
				int intNewComissions = 0;

				// System.out.println("Maximum Time: " + maxtime + " and  "
				// + intComissionedServers);

				for (Zone oZone : lstZone) {
					if (((oZone.computecount * 100) / maincomputecount) > 40) {
						// HBLB.zonetobecomissioned = oZone.zone;
						if (intComissionedServers == 1) {
							if (oZone.zone != HBLB.lstComissionedServer.get(0).oZone.zone) {
							System.out.println("GetZone");
								Zone newZone = Zone.getZoneObject(oZone.zone);

								newZone.lstServerUnderZone.get(0).isCommissioned = true;
								HBLB.lstComissionedServer.get(0).isCommissioned = false;
								//HBLB.lstComissionedServer = HBLB.sortLVMList();
								System.out.println("Sort LVM List");

							}
						} else {
							HBLB.lstComissionedServer.get(intNewComissions).isCommissioned = false;
							// Commissioning new server

							intNewComissions++;

						}

					}
				}
			}

		}

		catch (Exception ex) {

		}
		// System.out.println("This one is fine 4");

	}

	public void runthistokeepzonetrack(ServerInfo oServerInfo) {
		// your code here

		// System.out.println("runthistokeepzonetrack for server "
		// + oServerInfo.intServerID + "," + oServerInfo.isCommissioned);

		if (!oServerInfo.isCommissioned)
			return;

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
					.prepareCall("{call spgetset_zloadbalancer(?,?,?,?,?,?,?,?)}");
			// System.out.println("This one is fine 2");

			for (Zone oZone : oServerInfo.lstReqFromZone) {
				if (oZone.requestcount != 0) {
					synchronized (this) {
						cStmt.setInt(1, oZone.requestcount);
						cStmt.setString(2, HBLB.algoType);
						cStmt.setInt(3, oZone.memorycount);
						cStmt.setInt(4, oZone.storagecount);
						cStmt.setInt(5, oZone.computecount);
						cStmt.setString(6, oZone.zone);
						cStmt.setInt(7, oServerInfo.intServerID);
						cStmt.setString(8, "I");
						// System.out.println("This one is fine 3");
						boolean hadResults = cStmt.execute();
						oZone.requestcount = 0;
						oZone.memorycount = 0;
						oZone.storagecount = 0;
						oZone.computecount = 0;
					}
				}
			}
			if (!conn.isClosed()) {
				conn.close();
			}
			// System.out.println("This one is fine 4");

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
