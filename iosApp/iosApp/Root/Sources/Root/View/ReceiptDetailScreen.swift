//
//  ReceiptDetailScreen.swift
//  Root
//
//  Created by hagihara tomoaki on 2024/11/25.
//

import SwiftUI
import shared
import KmpShared

struct ReceiptDetailScreen: View {
    var targetReceipt: Receipt
    var feeCategoryList: [FeeCategory]
    var onEditReceipt: (Receipt) -> Void
    @Binding var isShowingItemScreen: Bool
    @Binding var headerState: HeaderState
    @State var selectedCategoryId: String
    @State var receiptCostText: String
    @State var costInputErrorMsg: String?
    let tag = "ReceiptDetailScreen"

    init(
        targetReceipt: Receipt,
        feeCategoryList: [FeeCategory],
        onEditReceipt: @escaping (Receipt) -> Void,
        isShowingItemScreen: Binding<Bool>,
        headerState: Binding<HeaderState>
    ) {
        KermitLoggerKt.d(tag: tag) { "init receipt: \(targetReceipt) " }
        self.targetReceipt = targetReceipt
        self.feeCategoryList = feeCategoryList
        self.onEditReceipt = onEditReceipt
        self._isShowingItemScreen = isShowingItemScreen
        self._headerState = headerState
        self.receiptCostText = String(targetReceipt.cost)
        // selectedCategoryはtargetReceiptをもとに生成
        let targetCategoryId = targetReceipt.category?.id ?? FeeCategory.companion.NoCategoryLabel
        self.selectedCategoryId = targetCategoryId
    }

    var body: some View {
        VStack {
            HeaderPanel(headerState: self.headerState)
            costTextField()
            categoryPicker()
            Spacer()
            Button("削除") {
                // todo
                self.isShowingItemScreen = false
            }.padding()
        }.onAppear {
            self.isShowingItemScreen = true
        }
        .onDisappear {
            self.isShowingItemScreen = false
        }
    }

    @ViewBuilder
    func categoryPicker() -> some View {
        HStack {
            Text("カテゴリ: ")
            Picker("カテゴリを選択", selection: $selectedCategoryId) {
                ForEach(feeCategoryList, id: \.id) { category in
                    Text(category.name)
                        .tag(category.id)
                }
                // 選択なし
                Text(FeeCategory.companion.NoCategoryLabel).tag(FeeCategory.companion.NoCategoryLabel)
            }.onChange(of: selectedCategoryId) { newCategoryId in
                onChangeSelectedCategory(newCategoryId: newCategoryId)
            }
            Spacer()
        }.padding()
    }

    func onChangeSelectedCategory(newCategoryId: String) {
        let nullableSelectedCategory = feeCategoryList.first { $0.id == selectedCategoryId }
        guard let newReceipt = try? Receipt(
            id: self.targetReceipt.id,
            cost: self.targetReceipt.cost,
            category: nullableSelectedCategory,
            createdAt: self.targetReceipt.createdAt
        ) else {
            KermitLoggerKt.e(tag: tag) {
                "failed to instantiate new receipt"
            }
            return
        }
        self.onEditReceipt(newReceipt)
    }

    @ViewBuilder
    func costTextField() -> some View {
        VStack(
            alignment: .leading,
            spacing: 2
        ) {
            Text("価格")
            TextField("100", text: $receiptCostText)
                .textFieldStyle(.roundedBorder)
                .keyboardType(.numberPad)
                .toolbar {
                    ToolbarItemGroup(placement: .keyboard) {
                        Spacer()
                        Button("完了") {
                            onEditCostCompleted()
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

    func onEditCostCompleted() {
        let intCost = Int(receiptCostText)
        guard let intCostValue = intCost else {
            self.costInputErrorMsg = "数値を入力してください"
            return
        }
        if intCostValue < 0 {
            self.costInputErrorMsg = "正の数値を入力してください"
            return
        }
        guard let newReceipt = try? Receipt(
            id: self.targetReceipt.id,
            cost: Int32(intCostValue),
            category: self.targetReceipt.category,
            createdAt: self.targetReceipt.createdAt
        ) else {
            KermitLoggerKt.e(tag: tag) {
                "failed to instantiate new receipt"
            }
            return
        }
        self.costInputErrorMsg = nil // 前回表示したエラーを削除
        self.onEditReceipt(newReceipt)
    }
}

#Preview {
    let pseudoFeeCategoryList: [FeeCategory] = [
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
    let pseudoReceipt = try? Receipt(
        id: "pseudo",
        cost: 100,
        category: pseudoFeeCategoryList.first,
        createdAt: ClockHelperKt.getCurrentTime()
    )
    ReceiptDetailScreen(
        targetReceipt: pseudoReceipt!,
        feeCategoryList: pseudoFeeCategoryList,
        onEditReceipt: { _ in },
        isShowingItemScreen: .constant(true),
        headerState: .constant(.error(msg: "this is header"))
    )
}
