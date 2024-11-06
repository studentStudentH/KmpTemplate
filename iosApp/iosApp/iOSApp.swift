import SwiftUI
import Root
import shared

@main
// swiftlint:disable:next type_name
struct iOSApp: App {
    init() {
        HelperKt.doInitKoin()
    }

	var body: some Scene {
		WindowGroup {
			ContentView()
		}
	}
}
