class Solution {
    public double[] calcEquation(List<List<String>> equations, double[] values, List<List<String>> queries) {
    
    Map<String, List<Pair<String, Double>>> graph = new HashMap<>();
    
    for( int i = 0; i < equations.size(); i++ ){
        
        String l = equations.get(i).get(0);
        String r = equations.get(i).get(1);
        
        graph.putIfAbsent(l, new ArrayList<>());
        graph.putIfAbsent(r, new ArrayList<>());
        
        graph.get(l).add(new Pair<>(r, values[i]));
        graph.get(r).add(new Pair<>(l, 1/values[i]));
        
    }
    
    double[] result = new double[queries.size()];
    
    int i = 0;
    
    for( List<String> q : queries ){
        result[i++] = find(q.get(0), new HashSet<>(), q.get(1), graph);
    }
    
    return result;
}

private double find( String curr, Set<String> visited, String dest, Map<String, List<Pair<String, Double>>> graph ){
    
    if( curr.equals(dest) ){
        return graph.containsKey(curr) ? 1.0 : -1.0;
    }
    
    visited.add(curr);
    
    for( Pair<String, Double> p : graph.getOrDefault(curr, new ArrayList<>()) ) {
        if( visited.contains(p.getKey()) ) continue;
        
        if( p.getKey().equals(dest) ) {
            return p.getValue();
        }else{
            double value = find( p.getKey(), visited, dest, graph );
            
            if( value != -1.0 ){
                return value*p.getValue();
            }
        }
        
    }
    
    return -1.0;
    
}
}