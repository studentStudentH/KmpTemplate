//
//  MainViewModel.swift
//  Root
//
//  Created by hagihara tomoaki on 2024/11/24.
//

import Foundation
@preconcurrency import shared
import KmpShared

@MainActor
public protocol IMainViewModel: ObservableObject {
    var headerState: HeaderState { get set }
    var receiptCollection: ReceiptCollection { get set }
    var feeCategoryList: [FeeCategory] { get set }

    func onAddReceipt(cost: Int)

    func onEditReceipt(editedReceipt: Receipt)

    func onDeleteReceipt(receipt: Receipt)
}

@MainActor
public class MainViewModel: ObservableObject, IMainViewModel {
    private let tag = "MainViewModel"
    private let loadingMsg = "loading..."
    private let updatingMsg = "updating..."
    private let addingMsg = "adding..."
    private let deletingMsg = "deleting..."
    private let updatedMsg = "successfully updated"
    private let addedMsg = "successfully added"
    private let deletedMsg = "successfully deleted"
    private let networkErrorMsg = "network error"
    private let criticalErrorMsg = "something wrong happened"
    private let delayMilliSecond = 700
    private let feeCategoryRepository: FeeCategoryRepository
    private let receiptRepository: ReceiptRepository
    @Published public var headerState: HeaderState = .none
    @Published public var receiptCollection: ReceiptCollection = .init(receipts: [])
    @Published public var feeCategoryList: [FeeCategory] = []

    public init(feeCategoryRepository: FeeCategoryRepository, receiptRepository: ReceiptRepository) {
        self.feeCategoryRepository = feeCategoryRepository
        self.receiptRepository = receiptRepository
        Task.init {
            // 同時にReceiptの方の読み込みもするのでheader表示は書き換えない
            let result = try? await feeCategoryRepository.getAllCategory()
            switch onEnum(of: result) {
            case .failure(let kmpFailure):
                KermitLoggerKt.e(tag: tag) {
                    "feeCategoryRepository.getAllCategory() failed error: \(kmpFailure.error.msg)"
                }
                self.headerState = .error(msg: networkErrorMsg)
            case .success(let kmpSuccess):
                KermitLoggerKt.d(tag: tag) {
                    "feeCategoryRepository.getAllCategory() success value: \(kmpSuccess.value)"
                }
                self.feeCategoryList = kmpSuccess.value.getMostRecentlyUsedList()
                self.headerState = .none
            case .none:
                KermitLoggerKt.e(tag: tag) { "feeCategoryRepository.getAllCategory() is nil" }
                self.headerState = .error(msg: criticalErrorMsg)
            }
        }
        loadReceipts(receiptRepository: self.receiptRepository)
    }

    /**
    self.receiptRepositoryを用いるとSendable関係で怒られるのでrepositoryを引数に取っている
     この挙動はSwift 6では改善されているらしい
     */
    private func loadReceipts(receiptRepository: ReceiptRepository) {
        self.headerState = .normal(msg: loadingMsg)
        Task.init {
            let result = try? await receiptRepository.getAllReceipts()
            switch onEnum(of: result) {
            case .failure(let kmpFailure):
                KermitLoggerKt.e(tag: tag) {
                    "receiptRepository.getAllReceipts() failed error: \(kmpFailure.error.msg)"
                }
                self.headerState = .error(msg: networkErrorMsg)
            case .success(let kmpSuccess):
                KermitLoggerKt.d(tag: tag) {
                    "receiptRepository.getAllReceipts() success value: \(kmpSuccess.value)"
                }
                self.receiptCollection = kmpSuccess.value
                self.headerState = .none
            case .none:
                KermitLoggerKt.e(tag: tag) { "receiptRepository.getAllReceipts() is nil" }
                self.headerState = .error(msg: criticalErrorMsg)
            }
        }
    }

    public func onAddReceipt(cost: Int) {
        onAddReceipt(
            cost: cost,
            feeCategoryRepository: self.feeCategoryRepository,
            receiptRepository: self.receiptRepository
        )
    }

    private func onAddReceipt(
        cost: Int,
        feeCategoryRepository: FeeCategoryRepository,
        receiptRepository: ReceiptRepository
    ) {
        Task.init {
            self.headerState = .normal(msg: addingMsg)
            guard let categoryResult = try? await feeCategoryRepository.getAllCategory() else {
                KermitLoggerKt.e(tag: tag) { "feeCategoryRepository.getAllCategory() result is nil" }
                self.headerState = .normal(msg: criticalErrorMsg)
                return
            }
            switch onEnum(of: categoryResult) {
            case .failure(let kmpFailure):
                KermitLoggerKt.e(tag: tag) { "feeCategoryRepository.getAllCategory() error: \(kmpFailure)" }
                self.headerState = .error(msg: networkErrorMsg)
            case .success(let kmpSuccess):
                KermitLoggerKt.d(tag: tag) { "feeCategoryRepository.getAllCategory() success" }
                let mostRecentlyUsed = kmpSuccess.value.getMostRecentlyUsedList().first
                let addResult = try? await receiptRepository.add(
                    cost: Int32(cost),
                    category: mostRecentlyUsed,
                    createdAt: ClockHelperKt.getCurrentTime()
                )
                switch onEnum(of: addResult) {
                case .success(let kmpSuccess):
                    KermitLoggerKt.d(tag: tag) { "receiptRepository.add() success value: \(kmpSuccess.value)" }
                    headerState = .normal(msg: addedMsg)
                    try? await Task.sleep(for: .milliseconds(delayMilliSecond))
                    loadReceipts(receiptRepository: receiptRepository)
                case .failure(let kmpFailure):
                    KermitLoggerKt.e(tag: tag) { "receiptRepository.add() error: \(kmpFailure)" }
                    self.headerState = .error(msg: networkErrorMsg)
                case .none:
                    KermitLoggerKt.e(tag: tag) { "receiptRepository.add() result is nil" }
                    self.headerState = .error(msg: criticalErrorMsg)
                }
            }
        }
    }

    public func onEditReceipt(editedReceipt: Receipt) {
        onEditReceipt(editedReceipt: editedReceipt, receiptRepository: self.receiptRepository)
    }

    private func onEditReceipt(editedReceipt: Receipt, receiptRepository: ReceiptRepository) {
        Task.init {
            self.headerState = .normal(msg: updatingMsg)
            let result = try? await receiptRepository.update(
                receiptId: editedReceipt.id,
                cost: editedReceipt.cost,
                category: editedReceipt.category,
                createdAt: editedReceipt.createdAt
            )
            switch onEnum(of: result) {
            case .success(let kmpSuccess):
                KermitLoggerKt.d(tag: tag) { "receiptRepository.update() success value: \(kmpSuccess.value)" }
                headerState = .normal(msg: updatedMsg)
                try? await Task.sleep(for: .milliseconds(delayMilliSecond))
                loadReceipts(receiptRepository: receiptRepository)
            case .failure(let kmpFailure):
                KermitLoggerKt.e(tag: tag) { "receiptRepository.update() faild value: \(kmpFailure.error)" }
                self.headerState = .error(msg: networkErrorMsg)
            case .none:
                KermitLoggerKt.e(tag: tag) { "receiptRepository.update() faild value is nil" }
                self.headerState = .error(msg: criticalErrorMsg)
            }
        }
    }

    public func onDeleteReceipt(receipt: Receipt) {
        onDeleteReceipt(receipt: receipt, receiptRepository: receiptRepository)
    }

    private func onDeleteReceipt(receipt: Receipt, receiptRepository: ReceiptRepository) {
        Task.init {
            self.headerState = .normal(msg: deletingMsg)
            let result = try? await receiptRepository.delete(receipt: receipt)
            switch onEnum(of: result) {
            case .success(let kmpSuccess):
                KermitLoggerKt.d(tag: tag) { "receiptRepository.delete() succeeded value = \(kmpSuccess.value)" }
                self.headerState = .normal(msg: deletedMsg)
                try? await Task.sleep(for: .milliseconds(delayMilliSecond))
                loadReceipts(receiptRepository: receiptRepository)
            case .failure(let kmpFailure):
                KermitLoggerKt.d(tag: tag) { "receiptRepository.delete() failed value = \(kmpFailure.error)" }
                self.headerState = .error(msg: networkErrorMsg)
            case .none:
                KermitLoggerKt.e(tag: tag) { "receiptRepository.delete() failed value is nil" }
                self.headerState = .error(msg: criticalErrorMsg)
            }
        }
    }
}
