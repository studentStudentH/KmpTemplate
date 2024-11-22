//
//  StatisticPanel.swift
//  Root
//
//  Created by hagihara tomoaki on 2024/11/23.
//

import SwiftUI

struct StatisticsPanel: View {
    let categorySummaryList: [CategorySummary]
    let totalCost: Int

    public init(categorySummaryList: [CategorySummary]) {
        self.categorySummaryList = categorySummaryList
        self.totalCost = categorySummaryList.map { $0.totalCost }.reduce(0, +)
    }

    var body: some View {
        VStack {
            Text("総出費")
                .font(.subheadline)
                .foregroundStyle(.secondary)
            Text(String(totalCost)).font(.largeTitle) + Text("円").font(.title)
            Spacer().frame(height: 16)
            ForEach(categorySummaryList) { categorySummary in
                CategorySummaryItem(categorySummary: categorySummary)
            }
        }
    }
}

private struct CategorySummaryItem: View {
    let categorySummary: CategorySummary

    var body: some View {
        HStack(
            alignment: .center
        ) {
            Text(categorySummary.categoryName).font(.title3)
            Spacer()
            Text(String(categorySummary.totalCost)).font(.title3) + Text("円")
        }
        .padding(.horizontal, 16)
        .padding(.vertical, 4)
    }
}

#Preview {
    let categorySummaryList: [CategorySummary] = [
        .init(totalCost: 10, categoryName: "食費"),
        .init(totalCost: 200, categoryName: "光熱費")
    ]
    StatisticsPanel(categorySummaryList: categorySummaryList)
}
