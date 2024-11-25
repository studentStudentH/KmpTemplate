//
//  ContentView.swift
//  Root
//
//  Created by hagihara tomoaki on 2024/11/04.
//

import SwiftUI
@preconcurrency import shared
import KmpShared

public struct ContentView: View {
    public init () {} // 他Packageからアクセスするために必要

    private let tag = "ContentView"

    @State var texts: [String] = ["loading"]

    public var body: some View {
        Text("hello")
        .task {
            await loadData()
        }.task {
            KermitLoggerKt.d(tag: tag) { "show body" }
        }.task {
            let localDateTime = getCurrentTime().toSystemLocalDateTime()
            KermitLoggerKt.d(tag: tag) { "localDateTime = \(localDateTime.dateTimeFormat())" }
        }
    }

    public func loadData() async {
        let repo: FeeCategoryRepository = DiContainer.shared.feeCategoryRepository
        do {
            let result = try await repo.getAllCategory()
            print("result; \(result)")
            switch onEnum(of: result) {
            case .failure(let kmpError):
                texts = [kmpError.error.msg]
            case .success(let successResult):
                texts = successResult.value
                    .getMostRecentlyUsedList()
                    .map { feeCategory in
                        feeCategory.name
                }
            }
        } catch {
            print("error: \(error)")
        }
    }
}

struct ContentView_Previews: PreviewProvider {
    static var previews: some View {
        ContentView()
    }
}
