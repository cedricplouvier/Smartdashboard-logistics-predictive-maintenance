# AI predicitve maintenance for logistics
## Functionality
A logistics company wants to optimize their fleet maintenance by implementing AI powered predictive maintenance for certain truck parts of their fleet.
Besides making prediction on the maintenance also a dashboard is needed to give necessary insights in their maintenance costs and strategy.

As a user you can request to predict a service window based on an unique ID or request relevant data regarding the fleet over a specified period of time.

For this I have build a java smart dashboard that parses all available PDF invoices and collected driving data from multiple 3rd party tools.
To get accurate insights I needed to couple all trucks to their used trailers for specific routes to enable calculation of over certain periods of time and link data from multiple tracking tools.


## Implementation & methodology

### 1. Data collection
- Parsing of Invoices from 2011 to 2021
- Transics data per trip (truck data)
  - Distances
  - Load
  - Consumption
- Sensolus (trailer tracking)
  - Address
  - lon & lat
  - Time
  - State: start, stop, on the move
 
### 2. Data transformation & Features
- Km travelled
- Motor time
- Driving time
- Stationary time
- Average speed
- Driving % with trailer
- Driving % with loaded trailer
- TARGET: weeks beetween repairs

### 3. Model training
An optimal regression pipeline is searched with AutoML tool TPOT

### 4. Results
See paper
