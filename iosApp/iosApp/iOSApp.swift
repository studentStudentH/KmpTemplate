import SwiftUI
import Root
import shared

@main
// swiftlint:disable:next type_name
struct iOSApp: App {
    let viewModel: MainViewModel
    
    init() {
        HelperKt.doInitKoin()
        viewModel = MainViewModel(
            feeCategoryRepository: DiContainer.shared.feeCategoryRepository,
            receiptRepository: DiContainer.shared.receiptRepository
        )
    }

	var body: some Scene {
		WindowGroup {
            TopScreen(viewModel: viewModel)
		}
	}
}
