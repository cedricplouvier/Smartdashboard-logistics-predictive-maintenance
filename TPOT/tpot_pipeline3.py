import numpy as np
import pandas as pd
from sklearn.ensemble import GradientBoostingRegressor, RandomForestRegressor
from sklearn.linear_model import RidgeCV
from sklearn.model_selection import train_test_split
from sklearn.pipeline import make_pipeline, make_union
from sklearn.preprocessing import MaxAbsScaler
from tpot.builtins import StackingEstimator
from tpot.export_utils import set_param_recursive

# NOTE: Make sure that the outcome column is labeled 'target' in the data file
tpot_data = pd.read_csv('PATH/TO/DATA/FILE', sep='COLUMN_SEPARATOR', dtype=np.float64)
features = tpot_data.drop('target', axis=1)
training_features, testing_features, training_target, testing_target = \
            train_test_split(features, tpot_data['target'], random_state=42)

# Average CV score on the training set was: -651.9192448027449
exported_pipeline = make_pipeline(
    StackingEstimator(estimator=RidgeCV()),
    MaxAbsScaler(),
    StackingEstimator(estimator=RandomForestRegressor(bootstrap=True, max_features=0.3, min_samples_leaf=9, min_samples_split=7, n_estimators=100)),
    GradientBoostingRegressor(alpha=0.8, learning_rate=0.1, loss="huber", max_depth=10, max_features=0.45, min_samples_leaf=3, min_samples_split=3, n_estimators=100, subsample=1.0)
)
# Fix random state for all the steps in exported pipeline
set_param_recursive(exported_pipeline.steps, 'random_state', 42)

exported_pipeline.fit(training_features, training_target)
results = exported_pipeline.predict(testing_features)
