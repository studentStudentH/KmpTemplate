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
        }.toolbar {
            ToolbarItem(placement: .bottomBar) {
                Button("Add") {
                    // actionはtodo
                }
            }
        }
    }
}

#Preview {
    TopScreen()
}
