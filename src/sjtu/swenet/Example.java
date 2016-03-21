package sjtu.swenet;

public class Example {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		System.out.println(SweNet.getInstance().sim("IOexception", "exception"));
		System.out.println(SweNet.getInstance().sim("database", "mysql"));
		System.out.println(SweNet.getInstance().sim("scripting-language", "python"));
		System.out.println(SweNet.getInstance().sim("animation", "flash"));
		System.out.println(SweNet.getInstance().sim("hibernate", "spring"));
		System.out.println(SweNet.getInstance().sim("angularjs", "web"));
		System.out.println(SweNet.getInstance().sim("css", "ubuntu"));
		System.out.println(SweNet.getInstance().sim("http", "nginx"));
	}
}
