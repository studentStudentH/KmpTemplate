//
//  ReceiptListItem.swift
//  Root
//
//  Created by hagihara tomoaki on 2024/11/23.
//

import SwiftUI
import shared
import KmpShared

struct ReceiptListItem: View {
    let receipt: Receipt

    public init(receipt: Receipt) {
        self.receipt = receipt
    }

    var body: some View {
        HStack {
            VStack(alignment: .leading, spacing: 4) {
                Text(String(receipt.cost)).font(.title3) + Text("円")
                Text(makeSubtitleString()).font(.subheadline).foregroundStyle(.secondary)
            }.foregroundStyle(.black)
            Spacer()
        }
    }

    private func makeSubtitleString() -> String {
        let dateLableString = receipt.createdAt.toSystemLocalDateTime().dateFormat()
        let categoryLableString = receipt.category?.name ?? ""
        return "\(dateLableString) \(categoryLableString)"
    }
}

#Preview {
    let receipt = Receipt.companion.makeInstanceForPreview(cost: 100, categoryName: "食費")
    ReceiptListItem(receipt: receipt).padding()
}
