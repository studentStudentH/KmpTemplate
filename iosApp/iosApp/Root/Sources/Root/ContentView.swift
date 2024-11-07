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

    private let sampleRepository = DiContainer.shared.sampleRepository

    @State var texts: [String] = ["loading"]

    public var body: some View {
        VStack {
            ForEach(texts, id: \.self) { text in
                Text(text)
            }
        }.task {
            await loadData()
        }.task {
            KermitLoggerKt.d(tag: tag) { "show body" }
        }
    }

    public func loadData() async {
        let repo = DiContainer.shared.sampleRepository
        do {
            let result = try await repo.getSampleWords()
            print("result; \(result)")
            switch onEnum(of: result) {
            case .failure(let kmpError):
                texts = [kmpError.error.msg]
            case .success(let successResult):
                texts = successResult.value.list.map { word in
                    word.description
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
