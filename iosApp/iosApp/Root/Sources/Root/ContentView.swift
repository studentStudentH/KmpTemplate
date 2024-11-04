//
//  ContentView.swift
//  Root
//
//  Created by hagihara tomoaki on 2024/11/04.
//

import SwiftUI
import shared

public struct ContentView: View {
    public init () {} // 他Packageからアクセスするために必要
    
    private let greet = Greeting().greet()

    public var body: some View {
        Text(greet)
    }
}

struct ContentView_Previews: PreviewProvider {
    static var previews: some View {
        ContentView()
    }
}
