//
//  HeaderPanel.swift
//  Root
//
//  Created by hagihara tomoaki on 2024/11/25.
//
import SwiftUI

struct HeaderPanel: View {
    let headerState: HeaderState

    var body: some View {
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
}
