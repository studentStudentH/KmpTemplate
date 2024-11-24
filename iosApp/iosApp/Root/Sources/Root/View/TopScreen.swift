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
    @State var receiptCollection: ReceiptCollection = ReceiptCollection.companion.makeInstanceForPreview()
    @State var addingReceiptCostText: String = ""
    @State var isSheetPresented: Bool = false
    @State var costInputErrorMsg: String?
    
    public init(viewModel: T) {
        self._viewModel = StateObject(wrappedValue: viewModel)
    }

    public var body: some View {
        let receipts = receiptCollection.sortByInstantDescending()
        let categorySummaryList = receiptCollection.splitByCategory().map {
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
                                destination: { Text("moved") },
                                label: { ReceiptListItem(receipt: receipt) }
                            )
                        }
                    }
                }
            }
        }.toolbar {
            ToolbarItem(placement: .bottomBar) {
                Button("Add") {
                    self.isSheetPresented = true
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
        let headerState: HeaderState = .error(msg: "pseudo header msg")
        // let headerState: HeaderState = .none
        switch headerState {
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
        // addする処理をここに書く
        self.isSheetPresented = false
    }
}

//#Preview {
//    TopScreen()
//}
