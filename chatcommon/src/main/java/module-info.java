module chatcommon {
	exports br.coffea.chat.common.utils;
	exports br.coffea.chat.common.rmi;
	exports br.coffea.chat.common;

	requires java.rmi;
	requires javafx.base;
	requires transitive javafx.controls;
	requires transitive javafx.fxml;
	requires javafx.graphics;
}