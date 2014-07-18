package rinkimai.pro;

public class VartotojoDuomenys 
{
	private static String time_stamp = null;
	private static String email = null;
	private static String password = null;
	private static String name = null;
	private static String surename = null;
	private static String user_id = null;
	private static String asm_kod = null;
	private static String bil_nr = null;
	
	public static boolean isLoginNeeded() {
		return !(email != null && password != null);
	}
	public static void dataFromSqlite(/*String time_stamp,*/String email,String password,String name
			,String surename,String user_id,String asm_kod,String bil_nr)
	{
		/*VartotojoDuomenys.time_stamp = time_stamp;*/
		VartotojoDuomenys.email = email;
		VartotojoDuomenys.password = password;
		VartotojoDuomenys.name = name;
		VartotojoDuomenys.surename = surename;
		VartotojoDuomenys.user_id = user_id;
		VartotojoDuomenys.asm_kod = asm_kod;
		VartotojoDuomenys.bil_nr = bil_nr;
	}
	
	public static boolean arPiliDuomenys()
	{
		return (/*time_stamp != null && */email != null && password != null && name != null &&
				surename != null && user_id != null && asm_kod != null && bil_nr != null);
	}
	
	public static String getEmail() {
		return email;
	}
	public static void setEmail(String email) {
		VartotojoDuomenys.email = email;
	}
	public static String getPassword() {
		return password;
	}
	public static void setPassword(String password) {
		VartotojoDuomenys.password = password;
	}
	public static String getName() {
		return name;
	}
	public static void setName(String name) {
		VartotojoDuomenys.name = name;
	}
	public static String getSurename() {
		return surename;
	}
	public static void setSurename(String surename) {
		VartotojoDuomenys.surename = surename;
	}
	public static String getUser_id() {
		return user_id;
	}
	public static void setUser_id(String user_id) {
		VartotojoDuomenys.user_id = user_id;
	}
	public static String getAsm_kod() {
		return asm_kod;
	}
	public static void setAsm_kod(String asm_kod) {
		VartotojoDuomenys.asm_kod = asm_kod;
	}
	public static String getBil_nr() {
		return bil_nr;
	}
	public static void setBil_nr(String bil_nr) {
		VartotojoDuomenys.bil_nr = bil_nr;
	}



	
}
