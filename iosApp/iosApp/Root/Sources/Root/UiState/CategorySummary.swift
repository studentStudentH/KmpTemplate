//
//  CategorySummary.swift
//  Root
//
//  Created by hagihara tomoaki on 2024/11/23.
//
import shared
import KmpShared

struct CategorySummary: Identifiable {
    let totalCost: Int
    let categoryName: String
    var id: String {
        get {
            self.categoryName
        }
    }
}

extension ReceiptCollectionPerCategory {
    func makeCategorySummary() -> CategorySummary {
        let castedTotalCost = Int(self.totalCost)
        return CategorySummary(totalCost: castedTotalCost, categoryName: self.categoryName)
    }
}