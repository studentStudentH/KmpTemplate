//
//  TopScreen.swift
//  Root
//
//  Created by hagihara tomoaki on 2024/11/23.
//
import SwiftUI
import shared
import KmpShared

struct TopScreen: View {
    @State var receiptCollection: ReceiptCollection = ReceiptCollection.companion.makeInstanceForPreview()

    var body: some View {
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
                    // actionはtodo
                }
            }
        }
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
        .frame(width: .infinity)
        .background(backgroundColor.edgesIgnoringSafeArea(.horizontal))
    }
}

#Preview {
    TopScreen()
}
