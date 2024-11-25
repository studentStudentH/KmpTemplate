//
//  TopScreen.swift
//  Root
//
//  Created by hagihara tomoaki on 2024/11/23.
//
import SwiftUI
import shared
import KmpShared

public struct TopScreen<T: IMainViewModel>: View {
    @StateObject var viewModel: T
    // @State var receiptCollection: ReceiptCollection = ReceiptCollection.companion.makeInstanceForPreview()
    @State var addingReceiptCostText: String = ""
    @State var isSheetPresented: Bool = false
    @State var costInputErrorMsg: String?
    @State var isShowingItemScreen: Bool = false

    public init(viewModel: T) {
        self._viewModel = StateObject(wrappedValue: viewModel)
    }

    public var body: some View {
        let receipts = viewModel.receiptCollection.sortByInstantDescending()
        let categorySummaryList = viewModel.receiptCollection.splitByCategory().map {
            $0.makeCategorySummary()
        }
        NavigationView {
            VStack {
                headerPanel()
                List {
                    Section(header: Text("統計")) {
                        StatisticsPanel(categorySummaryList: categorySummaryList)
                    }
                    Section(header: Text("明細一覧")) {
                        ForEach(receipts, id: \.id) { receipt in
                            NavigationLink(
                                destination: { itemScreen() },
                                label: { ReceiptListItem(receipt: receipt) }
                            )
                        }
                    }
                }
            }
        }.toolbar {
            if !isShowingItemScreen {
                ToolbarItem(placement: .bottomBar) {
                    Button("Add") {
                        self.isSheetPresented = true
                    }
                }
            }
        }.sheet(
            isPresented: $isSheetPresented,
            onDismiss: {
                self.addingReceiptCostText = ""
                self.costInputErrorMsg = nil
            },
            content: { addingPanel() }
        )
    }

    @ViewBuilder
    func headerPanel() -> some View {
        switch viewModel.headerState {
        case .none:
            EmptyView()
        case .normal(let msg):
            makeHeaderPanel(labelText: msg, labelColor: .white, backgroundColor: .blue)
        case .error(let msg):
            makeHeaderPanel(labelText: msg, labelColor: .white, backgroundColor: .red)
        }
    }

    @ViewBuilder
    func makeHeaderPanel(
        labelText: String,
        labelColor: Color,
        backgroundColor: Color
    ) -> some View {
        /// edgesIgnoringSafeAreaを設定しないとヘッダーの上まで色がついてしまう
        HStack {
            Text(labelText)
                .font(.headline)
                .foregroundStyle(labelColor)
                .padding(.all, 4)
            Spacer()
        }
        .background(backgroundColor.edgesIgnoringSafeArea(.horizontal))
    }

    @ViewBuilder
    func itemScreen() -> some View {
        Text("moved")
            .onAppear {
                self.isShowingItemScreen = true
            }
            .onDisappear {
                self.isShowingItemScreen = false
            }
    }

    @ViewBuilder
    func addingPanel() -> some View {
        VStack(
            alignment: .leading,
            spacing: 2
        ) {
            Text("価格を入力してください")
            TextField("100", text: $addingReceiptCostText)
                .textFieldStyle(.roundedBorder)
                .keyboardType(.numberPad)
                .toolbar {
                    ToolbarItemGroup(placement: .keyboard) {
                        Spacer()
                        Button("完了") {
                            onAddCompleted()
                        }
                    }
                }
            if let subText = self.costInputErrorMsg {
                Text(subText)
                    .font(.footnote)
                    .foregroundStyle(.red)
            } else {
                EmptyView()
            }
        }.padding()
    }

    func onAddCompleted() {
        let intCost = Int(addingReceiptCostText)
        guard let intCostValue = intCost else {
            self.costInputErrorMsg = "数値を入力してください"
            return
        }
        if intCostValue < 0 {
            self.costInputErrorMsg = "正の数値を入力してください"
            return
        }
        viewModel.onAddReceipt(cost: intCostValue)
        self.isSheetPresented = false
    }
}

private class MainViewModelForPreview: IMainViewModel {
    var headerState: HeaderState = .error(msg: "stub")
    var receiptCollection: ReceiptCollection = ReceiptCollection.companion.makeInstanceForPreview()
    var feeCategoryList: [FeeCategory] = [
        FeeCategory(
            id: "0",
            name: "光熱費",
            lastUsedAt: ClockHelperKt.getCurrentTime()
        ),
        FeeCategory(
            id: "1",
            name: "食費",
            lastUsedAt: ClockHelperKt.getCurrentTime()
        )
    ]
    var selectedReceipt: Receipt?

    func onReceiptSelected(receipt: Receipt) {
        // stub
    }

    func onAddReceipt(cost: Int) {
        // stub
    }

    func onEditReceipt(editedReceipt: Receipt) {
        // stub
    }

    func onDeleteReceipt(receipt: Receipt) {
        // stub
    }
}

#Preview {
    let mainViewModel = MainViewModelForPreview()
    TopScreen(viewModel: mainViewModel)
}
